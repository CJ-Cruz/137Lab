package backend.game.objects.units.buildings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import backend.game.objects.units.Building;
import backend.game.objects.units.res.SpriteLoader;
import frontend.game.Map;

public class Castle extends Building {
	
	public Castle(Point2D location, Map map, int owner, int id){
		this.id = id;
		this.type = 1;
		this.owner = owner;
		this.health = 25;
		this.width = 30;
		this.height = 30;
		location.setLocation(location.getX()+5, location.getY()+5);
		this.location = location;
		this.map = map;
	}
	
	public Castle(int id, int owner, int x, int y, int targetID, int health, int aQuantum, Map map) {
		this.id = id;
		this.type = 1;
		this.owner = owner;
		this.health = health;
		this.width = 30;
		this.height = 30;
		location = new Point(x, y);
		this.map = map;
	}

	@Override
	public void draw(Graphics g) {
		
		if(this.artmod == -1){
			g.setColor(Color.white);
			g.fillRect((int) (location.getX())-(width/2), (int) (location.getY()-(height/2)), width, height);
			artmod = 0;
		}
		else
			g.drawImage(SpriteLoader.buildings[0], (int) (location.getX()-(width/2)), (int) (location.getY()-(height/2)), null);
		
	}

}
