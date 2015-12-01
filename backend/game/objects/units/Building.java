package backend.game.objects.units;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Random;

import frontend.game.Map;
import utils.Globals;

public abstract class Building extends Unit implements Runnable, Globals{

	public String getDataString(boolean getMap){
		
		int targetID = -1;
		if(target != null)
			targetID = target.getID();
		
		String out = owner + "|" + type + "|" + id + "|" + health + "|" + targetID + "|" + (int)location.getX() + "|" + (int)location.getY() + "|" + attackQuantum + "|" + artmod;
		
		if(getMap){
			out += "|" + map.getIndex();
		}
		
		return out;
		
	}

	protected int Int(String s){
		return Integer.parseInt(s);
	}
	
	public int getID(){
		return id;
	}
	
	public void setTarget(int id){
		
		map.findUnit(id);
		
	}
	
	public Point2D getLocation(){
		return this.location;
	}
	
	abstract public void draw(Graphics g);
	
	@Override
	public void death(Unit attacker){
		
		map.kill(this.id);
		map.cancelTarget(id);
		map.updateBlocks();
		
	}
	
	public void damage(int atk, Unit attacker){
		
		health -= atk;
		artmod = -1;
		
		if(health <= 0)
			death(attacker);
		
	}
	
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
	
	public Unit getTarget(){
		return this.target;
	}
	
	public void attack(){
		
		Point2D target = this.target.getLocation();
		
		if(computeFinalDistance(target) <= this.range){
			
			if(textflag == 1){
				System.out.println(this + " has " + this.target + " in range");
				textflag = 2;
			}
			
			//attack
			if(attackQuantum >= attackQuantumMax){
				attackQuantum = 0;

				this.target.damage(attack, this);
				
			}
			attackQuantum++;
		}
		
	}
	
	@Override
	public void run(){
		
		//TODO: Create method of "enters within range" this.findTarget();
		
	}

	public int getOwner() {
		return owner;
	}
	
	
}
