package backend.game.objects.units;
import java.awt.Color;
//HazelCast | Run > Run Conf > ClassPath > Add Jar > Run
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Random;

import backend.game.objects.units.res.SpriteLoader;
import backend.game.system.AStar;
import backend.game.system.Bresenham;
import backend.game.system.Path;
import frontend.game.Map;
import utils.Globals;

public abstract class Troop extends Unit implements Runnable, Globals{
	
	protected int movespeed;
	protected int moveQuantum = 0;
	protected int moveQuantumMax = 1;
	
	protected boolean flying;
	protected boolean straight;
	protected Path path = null;
	protected Unit origTarget = null;
		
	public Troop(){
		art = ANIMATION_STAND_DOWN;
	}
	
//[Unit actions]===========================================	
	
	public void attack(){
		
		target.damage(attack, this);
		
	}
	
	private void specificMove(Point2D location, Point2D target){
		
		//Find direction
		double direction = Troop.computeDirection(location, target);
		
		int xplus = (int) Math.round(Math.cos(direction) * movespeed);
		int yplus =  (int) Math.round(Math.sin(direction) * movespeed);
		
		double angle = (direction*(180/Math.PI) + 360)%360;
		if(angle < 45 || angle >= 315){
			this.art = Globals.ANIMATION_STAND_RIGHT;
		}else if(angle < 135 && angle >= 45){
			this.art = Globals.ANIMATION_STAND_DOWN;
		}else if(angle < 225 && angle >= 135){
			this.art = Globals.ANIMATION_STAND_LEFT;
		}else if(angle >= 225 && angle < 315){
			this.art = Globals.ANIMATION_STAND_UP;
		}
		
		//Change anim
		art += artmod;
		
		if(artmod == 1)
			artmod = 2;
		else if(artmod == 2)
			artmod = 0;
		else
			artmod = 1;
		
		
		//next location
		this.location.setLocation(this.location.getX()+xplus, this.location.getY()+yplus);
		
	}
	
	public void move(){
		
		Point2D target = this.target.getLocation();
//		
//		Path t = path;
//		while(t != null){
//			System.out.println(t.getLocation());
//			t = t.getNext();
//		}
//		
		//base range = 10
		if(Bresenham.isClear(location, target, map.getBlocks(), getTargetID()) && computeFinalDistance(target) <= (this.range + 10)){
		
			double direction = Troop.computeDirection(location, target);
			artmod = 3;
			
			double angle = (direction*(180/Math.PI) + 360)%360;
			if(angle < 45 || angle >= 315){
				this.art = Globals.ANIMATION_STAND_RIGHT;
			}else if(angle < 135 && angle >= 45){
				this.art = Globals.ANIMATION_STAND_DOWN;
			}else if(angle < 225 && angle >= 135){
				this.art = Globals.ANIMATION_STAND_LEFT;
			}else if(angle >= 225 && angle < 315){
				this.art = Globals.ANIMATION_STAND_UP;
			}
			art += artmod;
			
//			if(textflag == 1){
//				System.out.println(this + " has " + this.target + " in range");
//				textflag = 2;
//			}
			
			if(attackQuantum >= attackQuantumMax){
				art += 1;
				attackQuantum = 0;
				this.attack();
			}

			attackQuantum++;
		}else{
			
			if(map.findUnit(this.target.getID()) == null){
				System.err.println("Dead Target");
				target = null;
				findTarget();
				return;
			}
			
//			if(textflag == 2){
//				System.out.println(this + " pursues " + this.target);
//				textflag = 1;
//			}
			
			if(moveQuantum >= moveQuantumMax){
				moveQuantum = 0;
				//A* to be added
				if(path == null && !straight){
					System.out.println("AStarNew");
					path = AStar.search(this, location, target, map.getBlocks());
				
				}else if(path != null){
					
					if(Bresenham.isClear(location, target, map.getBlocks(), getTargetID())){
						specificMove(location, target);
						straight = true;
						while(path.getNext() != null)
							path = path.getNext();
						return;
					}	
										
					
					Point2D pEnd = path.getLastLocation();
					if(Math.abs(pEnd.getX()-target.getX()) > 5 && Math.abs(pEnd.getY()-target.getY())>5){
						System.out.println("AStarring Again");
						path = AStar.search(this, location, target, map.getBlocks());
					}
//					
					if(Math.abs(path.getLocation().getX() - location.getX()) < 2 && Math.abs(path.getLocation().getY() - location.getY()) < 2){
						path = path.getNext();
					}
					
					else specificMove(location, path.getLocation());
					
				}else specificMove(location, target);
			}else
				moveQuantum++;
			}
	}
	
	public void findTarget(){
		if(target == null){
			
			if(targetID == -1){
			
				target = map.findTarget(location, owner, priority, id);
				if(target != null){	//new Target found
					System.out.println(this.toString() + " is moving to "+ target);
					straight = false;
					
					System.out.println("Finding AStar path");
					path = AStar.search(this, location, target.getLocation(), map.getBlocks());
					
					textflag = 1;
					move();
				}
				
			}else{
				Unit temp = map.findUnit(targetID);
				if(temp != null)
					target = temp;
			}
			
		}else move();
		
		
	}

//[Threads]=====================================
	
	@Override
	public void run(){
		this.findTarget();
	}
	
	@Override
	public void draw(Graphics g){
		
		Color player = null;
		switch(owner){
		case 0: player = PLAYER_1;
			break;
		case 1: player = PLAYER_2;
			break;
		case 2: player = PLAYER_3;
			break;
		}
		
		
		if(this.artmod == -1){
			g.setColor(Color.white);
			artmod = 0;
			g.fillRect((int)this.location.getX()-(height/2), (int)this.location.getY()-(height/2), width, height);
		}else
			g.drawImage(SpriteLoader.sprites[type][art], (int)this.location.getX()-(width/2), (int)this.location.getY()-(height), player,null);
	}
	
}
