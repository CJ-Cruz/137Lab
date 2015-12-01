package backend.game.system;

import java.awt.geom.Point2D;

public class Path {

	private Point2D location;
	private Path next;
	private Path prev;
	
	public Path(Point2D location){
		this.location = location;
		this.next = null;
		this.prev = null;
	}
	
	public Path(Point2D location, Path next){
		this.location = location;
		this.prev = null;
		this.next = next;
	}

	public void previous(Path next2) {
		this.prev = next2;
	}

	public Path getNext() {
		if(next == null)
			return null;
		return next;
	}

	public void setNext(Path temp) {
		this.next = temp;
	}

	public Point2D getLocation() {
		return location;
	}
	
	public Point2D getLastLocation(){
		
		Path temp = this;
		
		while(temp.getNext() != null){
			temp = temp.getNext();
		}
		
		return temp.getLocation();
		
	}
	
	public void setPrev(Path p){
		prev = p;
		if(p != null)
			p.next = this;
	}

	@Override
	public String toString(){
		String out = "";
		Path temp = this;
		while(temp != null){
			Point2D loc = temp.getLocation();
			out += "," + (int) loc.getX() + "," + (int) loc.getY();
			temp = temp.getNext();
			
		}
		
		return out;
		
	}
	
}
