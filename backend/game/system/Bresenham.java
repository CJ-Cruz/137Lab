package backend.game.system;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import frontend.game.Map;

public class Bresenham {
	
	public static boolean isClear(Point2D start, Point2D end, int[][] blocks, int targID){
		
		//start points
		int x = (int) Math.floor(start.getX()/10);
		int y = (int) Math.floor(start.getY()/10);
		
		//end points
		int xE = (int) Math.floor(end.getX()/10);
		int yE = (int) Math.floor(end.getY()/10);
		
		int steps = 0;
		
		int mY = Math.abs((yE - y) * 2);
		int mX = Math.abs((xE - x) * 2);

		int tY = 1;
		int tX = 1;
		
		int eY = 0;
		int eX = 0;
		
		boolean xfirst = mY<mX?true:false;
		
		while(true){
			
			if(x == xE && y == yE)
				break;
			
			if(xfirst){
				
				eX += mX;
				if(eX > tX){
					tX += 2;
					
					if(xE < x)
						x--;
					else
						x++;
				}
				
				steps++;
				if(blocks[x][y]!=0 && blocks[x][y]!=1 && blocks[x][y]!=targID){
					return false;
				}
			}
			
			if(x == xE && y == yE)
				break;
			
			eY += mY;
			if(eY > tY){
				tY += 2;
				if(yE < y)
					y--;
				else
					y++;
			}
			
			xfirst = true;
			steps++;
			if(blocks[x][y]!=0 && blocks[x][y]!=1 && blocks[x][y]!=targID){
				return false;
			}
			
		}
		return true;

	}
	
}
