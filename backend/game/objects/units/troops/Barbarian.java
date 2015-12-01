package backend.game.objects.units.troops;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import backend.game.objects.units.Troop;
import backend.game.objects.units.res.SpriteLoader;
import backend.game.system.Path;
import frontend.game.Map;
import utils.Globals;

public class Barbarian extends Troop implements Globals{

	public void initialize(){
		
		this.type = TROOP_BASIC;
		this.movespeed = 2;
		this.width = 16;
		this.height = 16;
		this.range = 20;
		this.attackQuantumMax = 10;
		this.moveQuantumMax = 2;
		this.priority = PRIORITY_NEAREST_NOT_WALL;
		
	}
	
	public Barbarian(Point2D location, Map map, int owner, int id){
		
		super();
		
		this.id = id;
		this.owner = (byte) owner;
		this.health = 15;
		this.setLocation(location);
		this.map = map;
		
		initialize();
	}

	public Barbarian(int id, int owner, int x, int y, int targetID, int health, int aQuantum, int mQuantum, Path p, Map map) {
		
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
