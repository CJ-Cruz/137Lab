package backend.game.objects.units.buildings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import backend.game.objects.units.Building;
import backend.game.objects.units.res.SpriteLoader;
import frontend.game.Map;

public class Wall extends Building {
	
	public Wall(Point2D location, Map map, int owner, int id){
		this.id = id;
		this.type = 2;
		this.owner = owner;
		this.health = 50;
		this.width = 10;
		this.height = 10;
		this.setLocation(location);
		this.map = map;
	}
	
	public Wall(int id, int owner, int x, int y, int targetID, int health, int aQuantum, Map map) {
		this.id = id;
		this.type = 2;
		this.owner = owner;
		this.health = health;
		this.width = 10;
		this.height = 10;
		this.setLocation(new Point(x,y));
		this.map = map;
	}

	@Override
	public void draw(Graphics g) {
		
		if(this.artmod == -1){
			g.setColor(Color.white);
			g.fillRect((int) (location.getX()), (int) (location.getY()), width, height);
			artmod = 0;
		}
		else{
			g.setColor(Color.gray);
			g.fillRect((int) (location.getX()), (int) (location.getY()), width, height);
		}
	}

}
