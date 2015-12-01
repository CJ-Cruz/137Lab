package backend.game.objects.units;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Random;

import backend.game.objects.units.buildings.Castle;
import backend.game.objects.units.buildings.Wall;
import backend.game.objects.units.troops.Archer;
import backend.game.objects.units.troops.Barbarian;
import backend.game.objects.units.troops.Giant;
import backend.game.system.Path;
import backend.system.GSystem;
import backend.system.Server;
import frontend.game.Map;
import utils.Globals;

public abstract class Unit implements Runnable, Globals{
	
	//Attributes to be sent
	protected int id;
	protected int owner;
	protected int type;
	protected Point2D location;
	protected int health = 100;
	protected Unit target;
	protected int attackQuantum = 0;
	//Dynamic
	protected Map map;
	protected int targetID = -1;
	protected int attack = 5;
	protected int priority;
	protected int width;
	protected int height;
	protected int range = 20;
	protected int artmod = 0;
	protected int art;
	protected int attackEffect;
	protected int textflag;
	protected int attackQuantumMax = 2;
	
	//TODO:	protected Projectile projectile;

//[Abstracts]====================================
	
	abstract public void draw(Graphics g);
	
	@Override
	abstract public void run();
	
	abstract public void attack();
	
//[Get/Set]======================================
	
	public int getID(){
		return id;
	}
	public void setTarget(int id){
		target = map.findUnit(id);	
	}
	public void setLocation(Point2D location){
		this.location = location;
	}
	public Point2D getLocation(){
		return this.location;
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
	public int getOwner() {
		return owner;
	}
	public int getTargetID() {
		if(target != null)
			return target.getID();
		return -1;
	}
	
//[Unit actions]======================================
	
	public void death(Unit attacker){
		
		map.cancelTarget(id);
		map.kill(this.id);
		
		if(attacker instanceof Troop){
			Troop u = (Troop) this;
			if(u.origTarget != null){
				target = u.origTarget;
				u.origTarget = null;
				
			}else{
				u.targetID = -1;
			}
				
		}//TODO: Attacker instanceof Building
		
		GSystem sys = map.getSystem();
		if(sys instanceof Server)
			((Server) sys).updateClients();
		
	}

	public void damage(int atk, Unit attacker){
		
		health -= atk;
		artmod = -1;
		
		if(health <= 0)
			death(attacker);
		
	}
	
	public void stopFollow() {
		this.target = null;
		this.targetID = -1;
	}
	public void targetUnitBlock(int x, int y){
		target = map.getBuildingAt(x, y);
	}
	public void targetNearest(int x, int y) {
		Point2D loc = new Point();
		loc.setLocation(x, y);
		Troop u = (Troop) this;
		u.origTarget = target;
		this.target = map.findTarget(loc, owner, PRIORITY_NEAREST, id);
		System.out.println("target changed for " + this + ": " + target.getID() + "; old - " + u.origTarget.getID());
	}
	
//[Unit Computation Utilities]======================================
		
	public double computeFinalDistance(Point2D target){
		double opDirection = Unit.computeDirection(target, location);
		int wplus = (int) Math.round(Math.cos(opDirection) * (this.target.getWidth()/1.8));
		int hplus =  (int) Math.round(Math.sin(opDirection) * (this.target.getHeight()/1.8));
		Point2D arbitrary = new Point();
		arbitrary.setLocation(target.getX()+wplus, target.getY()+hplus);
		return this.getLocation().distance(arbitrary);
	}
	
	public static double computeDirection(Point2D start, Point2D end){
		
		double direction = Math.atan2(end.getY()-start.getY(), end.getX()-start.getX());
		
		return direction;
		
	}

//[Client/Server Utilities]======================================	
	
	@Override
	public String toString(){
		
		String out = "";
	
		if(this instanceof Troop){
			
			Troop u = (Troop) this;
			
			int tid = -1;
			if(target!=null && target.getID()!=-1)
				tid = target.getID();
			
			String path = "";
			if(u.path!=null)
				path = u.path.toString();
			
			out += this.id + "," + 
					this.type + "," +
					this.owner + "," +
					(int) this.location.getX() + "," + 
					(int) this.location.getY() + "," + 
					tid + "," + 
					this.health + "," + 
					this.attackQuantum + "," +
					u.moveQuantum +
					path;
			
		}
		else if(this instanceof Building){
			
			Building u = (Building) this;
			
			int tid = -1;
			if(target!=null && target.getID()!=-1)
				tid = target.getID();
			
			out += this.id + "," + 
					this.type + "," +
					this.owner + "," +
					(int) this.location.getX() + "," + 
					(int) this.location.getY() + "," +
					tid + "," + 
					this.health + "," + 
					this.attackQuantum;
			
			System.out.println(out);
			
		}
		
		return out;
		
	}
	
	public static Unit recreate(int id, int type, int owner, int x, int y, int targetID, int health, int aQuantum, int mQuantum, Path p, Map map, boolean building){
		
		if(!building)
			switch(type){
			
			case TROOP_BASIC: return new Barbarian(id, owner, x, y, targetID, health, aQuantum, mQuantum, p, map);
			case TROOP_ARCHER: return new Archer(id, owner, x, y, targetID, health, aQuantum, mQuantum, p, map);
			case TROOP_GIANT: return new Giant(id, owner, x, y, targetID, health, aQuantum, mQuantum, p, map);
				
			default: return null;
			
			}
		else
			switch(type){
			
			case 1: return new Castle(id, owner, x, y, targetID, health, aQuantum, map);
			case 2: return new Wall(id, owner, x, y, targetID, health, aQuantum, map);
			
			default: return null;
			
			}
		
	}
	
//[Deprecated/Do Not Use]==================================

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	
}