package backend.game.objects.units.troops;

import java.awt.Point;
import java.awt.geom.Point2D;

import backend.game.objects.units.Troop;
import backend.game.system.Path;
import frontend.game.Map;

public class Giant extends Troop {

public void initialize(){
		
		this.type = TROOP_GIANT;
		this.movespeed = 2;
		this.width = 24;
		this.height = 24;
		this.range = 40;
		this.attackQuantumMax = 10;
		this.moveQuantumMax = 2;
		this.priority = PRIORITY_NEAREST_NOT_WALL;
		
	}
	
	public Giant(Point2D location, Map map, int owner, int id){
		
		super();
		
		this.id = id;
		this.owner = (byte) owner;
		this.health = 50;
		this.attack = 20;
		this.setLocation(location);
		this.map = map;
		
		initialize();
	}

	public Giant(int id, int owner, int x, int y, int targetID, int health, int aQuantum, int mQuantum, Path p, Map map) {
		
		initialize();
		
		this.map = map;
		this.id = id;
		this.owner = owner;
		Point2D loc = new Point();
		loc.setLocation(x, y);
		this.targetID = targetID;
		this.location = loc;
		this.health = health;
		this.attackQuantum = aQuantum;
		this.moveQuantum = mQuantum;
		this.path = p;
		
	}
	
}
