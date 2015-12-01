package backend.game.system;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import backend.game.objects.units.Troop;
import backend.game.objects.units.Building;
import backend.game.objects.units.Unit;
import backend.game.objects.units.buildings.Castle;
import backend.game.objects.units.buildings.Wall;
import backend.game.objects.units.troops.Archer;
import backend.game.objects.units.troops.Barbarian;
import backend.game.objects.units.troops.Giant;
import backend.system.Client;
import frontend.game.Map;
import utils.Globals;
public class State implements Globals{

	private Map map;
	private LinkedList<Unit> units;
	private int id = 1;

	private int castleCount = 4;
	private int wallCount = 20;
	private int barbarianCount = 20;
	private int archerCount = 10;
	private int giantCount = 10;
	
	public State(){
		units = new LinkedList<Unit>();
	}
	
//[Unit Utilities]===============================================
	
	public Unit find(int uid) {
		
		for(int i = 0; i < units.size(); i++){
			
			if(units.get(i).getID() == uid)
				return units.get(i);
			
		}
		return null;
		
	}
	
	public Unit find(Point2D location, int owner, int priority, int uid){
		
		double minDist = Integer.MAX_VALUE;
		Unit min = null;
		Unit bldg = null;
		Unit def = null;
		Unit wall = null;
		Unit notwall = null;
		
		for(int i = 0; i < units.size(); i++){
			
			double temp = 0;
//				if(priority == PRIORITY_NEAREST){
				try{
					temp = AStar.manhattan(new sPoint(units.get(i).getLocation()), new sPoint(location));
				
//				}else
//					temp = units.get(i).getLocation().distance(location);
				
					if((units.get(i).getID() != uid && temp < minDist)&&(owner != units.get(i).getOwner())){
						minDist = temp;
						min = units.get(i);
						if(min instanceof Wall)
							wall = min;
						else {
							if(min instanceof Building)
								bldg = min;
							
							notwall = min;
							
						}
					}
				}catch(Exception e){return null;}
		}

		switch(priority){

		case PRIORITY_BUILDINGS:
			return bldg;
		case PRIORITY_WALL:
			return wall;
		case PRIORITY_DEFENSES:
			return bldg;
		case PRIORITY_NEAREST_NOT_WALL:
			return notwall;
			//Nearest
		default: return min;
		
		}
		
	}

	public void addUnit(int type, Point2D location, int owner, boolean build){
		if(build){
			switch(type){
			
			case 1: units.add(new Castle(location, map, owner, ++id));
				System.out.println(id);
				break;
			case 2: units.add(new Wall(location, map, owner, ++id));
				System.out.println(id);
				break;
		
			}
		}
		else
		switch(type){

		case 1: units.add(new Barbarian(location, map, owner, ++id));
			break;
		case 2: units.add(new Archer(location, map, owner, ++id));
			break;
		case 3: units.add(new Giant(location, map, owner, ++id));
			break;
		
		}
				
	}
	
	public Building findBuilding(int id) {
		
		for(int i = 0; i < units.size(); i++){
			if(id == units.get(i).getID())
				return (Building) units.get(i);
		}
		
		return null;
		
	}

	public void removeBuilding(Building b){
		
		//TODO: Get building type then recover back the number
		units.remove(units.indexOf(b));
		
	}

	public void remove(int id) {
		
		//TODO: animate death
		
		
		for(int i = 0; i < units.size(); i++){
			if(units.get(i).getID() == id){
				units.remove(i);
				break;
			}
		}
		
	}

	public void stopFollowers(int id2) {
		
		for(int i = 0; i < units.size(); i++){
			if(units.get(i).getTargetID() == id2)
				units.get(i).stopFollow();
		}
		
	}

	
//[Get/Set]==============================================
	
	public void setMap(Map m){
		map = m;
	}
	
	public int getUnitSize() {
		return this.units.size();
	}

	public Unit getUnit(int i) {
		try{
			return units.get(i);
		}catch(Exception e){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return units.get(i);
		}
	}

	public int getID() {
		return id+1;
	}

//[Client/Server]===================================================	
	
	public String compile() {
		
		String out = "M";
		
		boolean unitmode = false;
		for(int i = 0; i < units.size(); i++){
			if(!unitmode && !(units.get(i) instanceof Building)){
				out+="T";
				unitmode = true;
			}
				
			out += units.get(i).toString()+"|";
			
			
		}
		if(!unitmode)	//Add delimiter
			out+="T";
		
		return out;
	
	}
	
	public void update(String map){
		
		System.out.println("Updating using " + map);
		
		if(map.equals("")||map.equals("T"))
			return;
		
		String[] split = map.split("T");

		String buildings = split[0];
		String[] building = buildings.split("\\|");

		units.clear();
		
		if(!buildings.equals(""))
		for(int i = 0; i < building.length; i++){
			
			String[] tdata = building[i].split(",");
			
			Path temp = null;
			Path p = null;
			for(int j = 9; j < tdata.length; j+=2){
			
				Point2D loc = new Point();
				loc.setLocation(Integer.parseInt(tdata[j]), Integer.parseInt(tdata[j+1]));
				
				p = null;
				
			}
			
			Unit rc = Unit.recreate(Integer.parseInt(tdata[0]), Integer.parseInt(tdata[1]), Integer.parseInt(tdata[2]), 
					Integer.parseInt(tdata[3]), Integer.parseInt(tdata[4]), Integer.parseInt(tdata[5]), Integer.parseInt(tdata[6]), 
					Integer.parseInt(tdata[7]), -1, p, this.map, true);
			
			units.add(rc);
			this.map.updateBlocks();
			
			id = Integer.parseInt(tdata[0])+1;
			
		}
		
		try{
			String troops = split[1];
			String[] troop = troops.split("\\|");
			
			for(int i = 0; i < troop.length; i++){
				
				String[] tdata = troop[i].split(",");
				
				Path temp = null;
				Path p = null;
				for(int j = 9; j < tdata.length; j+=2){
				
					Point2D loc = new Point();
					loc.setLocation(Integer.parseInt(tdata[j]), Integer.parseInt(tdata[j+1]));
					
					p = new Path(loc);
					p.setPrev(temp);
					temp = p;
					
				}
				
				Unit rc = Unit.recreate(Integer.parseInt(tdata[0]), Integer.parseInt(tdata[1]), Integer.parseInt(tdata[2]), 
						Integer.parseInt(tdata[3]), Integer.parseInt(tdata[4]), Integer.parseInt(tdata[5]), Integer.parseInt(tdata[6]), 
						Integer.parseInt(tdata[7]), Integer.parseInt(tdata[8]), p, this.map, false);
				
				units.add(rc);
				
				id = Integer.parseInt(tdata[0])+1;
				
			}
		}catch(IndexOutOfBoundsException e){}
		
		
	}
	
}
