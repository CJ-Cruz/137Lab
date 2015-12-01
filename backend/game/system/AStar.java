package backend.game.system;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import backend.game.objects.units.Unit;
import frontend.game.Map;


public class AStar {

	private static LinkedList<sPoint> open;
	private static LinkedList<sPoint> closed;

	public static Path search(Unit u, Point2D position, Point2D target, int[][] blocks){
		
		Path blocker;
		int rep = 0;
		double exmin = Integer.MAX_VALUE;
		LinkedList<sPoint> finished = new LinkedList<sPoint>();
		open = new LinkedList<sPoint>();
		closed = new LinkedList<sPoint>();
		
		sPoint start = new sPoint((int)position.getX()/10, (int)position.getY()/10);
		sPoint end = new sPoint((int)target.getX()/10, (int)target.getY()/10);
		
		open.add(start);
		
		while(!open.isEmpty()){
			
			sPoint current = getMinimum();
			closed.push(current);
			if(exmin < current.g){
				
				rep++;
				if(rep > 500){
					//System.out.println("Maxima");
					return stopSearch(finished, end, u, start, blocks);
				}
				
			}
			else{ 
				rep = 0;
				exmin = current.g;
			}
			
			//System.out.println(current.x + ", " + current.y);
			
			//Goal State
			if(current.equalsC(end)){
				//System.out.println("Path Found!");
				return minimize(createPath(start, current), blocks, u);
			}
			
			LinkedList<sPoint> neighbors = check(current, blocks, u);
			
			for(int i = 0; i < neighbors.size(); i++){
				
				sPoint subcurrent = neighbors.get(i);
				subcurrent.parent = current;
				if(!subcurrent.in(finished)){
					
					subcurrent.g = current.g + euclidean(subcurrent, current);
					subcurrent.f = subcurrent.g + euclidean(subcurrent, end);
					open.push(subcurrent);
					finished.add(subcurrent);
					
				}
				
			}
			closed.push(current);
			
		}
		return stopSearch(finished, end, u, start, blocks);
		
	}
	
//[Heuristics]==================================================	
	
	public static double euclidean(sPoint start, sPoint end){
		
		return Math.sqrt(Math.pow(Math.abs(start.x - end.x), 2) + Math.pow(Math.abs(start.y - end.y),2));
		
	}

	public static double manhattan(sPoint start, sPoint end){
	
		return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
	
	}

//[Utilities]====================================================	
	
	public static LinkedList<sPoint> check(sPoint p, int[][] blocks, Unit u){
		
		LinkedList<sPoint> results = new LinkedList<sPoint>();
		int x = p.x;
		int y = p.y;
		
		if(y-1 > -1 )
			if((blocks[x][y-1]==0 || blocks[x][y-1]==1) || blocks[x][y-1] == u.getTargetID())
				results.push(new sPoint(x, y-1));
			
		if(y+1 < blocks[x].length)
			if((blocks[x][y+1]==0 || blocks[x][y+1]==1)  || blocks[x][y+1] == u.getTargetID())
				results.push(new sPoint(x, y+1));
		
		if(x-1 > -1 )
			if((blocks[x-1][y]==0 || blocks[x-1][y]==1)  || blocks[x-1][y] == u.getTargetID())
				results.push(new sPoint(x-1, y));
		
		if(x+1 < blocks.length)
			if((blocks[x+1][y]==0||blocks[x+1][y]==1)  || blocks[x+1][y] == u.getTargetID())
				results.push(new sPoint(x+1, y));
		
		return results;
		
	}

	private static Path stopSearch(LinkedList<sPoint> finished, sPoint end, Unit u, sPoint start, int[][] blocks) {
		sPoint nearest = null;
		double eucl = Double.MAX_VALUE;
		while(!finished.isEmpty()){
			
			sPoint temp = finished.removeFirst();
			double cal = euclidean(temp, end);
			if(eucl > cal){
				eucl = cal;
				nearest = temp;
			}
			
		}
		
		//System.out.println("Target Wall");
		u.targetNearest(nearest.x*10, nearest.y*10);
		return minimize(createPath(start, nearest), blocks, u);
	}

	public static Path createPath(sPoint start, sPoint end){
		Path next = null;
		
		while(!end.equalsC(start)){
			
			if(next == null){
				Point2D location = new Point();
				location.setLocation((end.x*10)+5, (end.y*10)+5);
				next = new Path(location);
			}else{
				Point2D location = new Point();
				location.setLocation((end.x*10)+5, (end.y*10)+5);
				Path temp = next;
				next = new Path(location, next);
				temp.previous(next);
			}
			end = end.parent;
			
		}
		
		return next;
		
	}

	public static Path minimize(Path p, int[][] blocks, Unit u){
		
		Path t = p;
//		System.out.println("[Path minimizing]------------ \nPath:");
//		while(t != null){
//			System.out.println(t.getLocation());
//			t = t.getNext();
//		}
		
		Path head = p;
		//
		while(p.getNext() != null){
		
			Path temp = p;
			if(temp.getNext() != null)
				temp = p.getNext();
			while(temp != null){
				
				if(Bresenham.isClear(p.getLocation(), temp.getLocation(), blocks, u.getTargetID())){
					p.setNext(temp);
					temp = temp.getNext();
				}else{
					break;
				}
				
			}
			
			p = p.getNext();
			if(p.getNext() == null)
				break;
		}
		//*/
		Path trace = head;
//		System.out.println("Minimized:");
	//	while(trace != null){
	//		System.out.println(trace.getLocation());
	//		trace = trace.getNext();
	//	}
		
		return head;
		
	}
	
	public static sPoint getMinimum(){
		int mindex = 0;
		double min = Integer.MAX_VALUE;
		for(int i = 0; i < open.size(); i++){
			if(open.get(i).f < min){
				min = open.get(i).f;
				mindex = i;
			}
				
		}
		
		return open.remove(mindex);
		
	}
	
}

class sPoint{
	
	sPoint parent;
	int x;
	int y;
	double g = 0;
	double f = 0;
	
	public sPoint(Point2D loc){
		x = (int) loc.getX()/10;
		y = (int) loc.getY()/10;
	}
	
	public sPoint(int x2, int y2) {
		
		x = x2;
		y = y2;

	}

	public boolean in(LinkedList<sPoint> list){
		
		for(int i = 0; i < list.size(); i++){
			if(this.equalsC(list.get(i)))
				return true;
		}
		return false;
		
	}
	
	public boolean equalsC(sPoint p){
		
		if(x == p.x && y == p.y)
			return true;
		
		return false;
		
	}
	
}