package frontend.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.IOException;

import javax.swing.JPanel;

import backend.game.objects.units.Building;
import backend.game.objects.units.Troop;
import backend.game.objects.units.Unit;
import backend.game.objects.units.res.SpriteLoader;
import backend.game.system.AStar;
import backend.game.system.Path;
import backend.game.system.State;
import backend.system.Client;
import backend.system.GSystem;
import backend.system.Server;

public class Map extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;
	private int index;
	private State state;
	private boolean user;
	private GSystem system;
	private Thread thread;
	private int[][] blocks;
	private int[][] path;
	
	public Map(GSystem sys, State s, int index, boolean user){
		
		this.setBackground(Color.white);
		system = sys;
		state = s;
		state.setMap(this);
		this.index = index;
		this.user = user;
		thread = new Thread(this);
		thread.start();
		this.setPreferredSize(new Dimension(350,350));
		blocks = new int[(int) (this.getPreferredSize().getWidth()/10)][(int) (this.getPreferredSize().getHeight()/10)];
		for(int i = 0; i < blocks.length; i++){
			for(int j = 0; j < blocks[i].length; j++){
				blocks[i][j] = 0;
			}
		}
		
		path = blocks.clone();
		
	}
	
//[Get/Set]=========================================

	public Building getBuildingAt(int x, int y){
		return state.findBuilding(blocks[x][y]);	
	}
	public int getIndex(){
		return index;
	}
	public int getId(){
		return this.state.getID();
	}
	public GSystem getSystem() {
		return system;
	}
	public int[][] getBlocks() {
		return blocks;
	}

	
//[Pathing]=========================================
	
	public void block(Point2D location, int id, int width, int height){
		
		int centerx = (int) (location.getX()/10);
		int centery = (int) (location.getY()/10);
		
		int lx = centerx - (width/10/2);
		int rx = centerx + (width/10/2) + 1;
		int ty = centery - (height/10/2);
		int by = centery + (height/10/2) + 1;
		
		for(int i = lx; i < rx; i++){
			for(int j = ty; j < by; j++){
				blocks[i][j] = id;
			}
		}
		
		
	}
	
	public boolean isBlocked(int x, int y){
		return blocks[x][y]!=0;
	}
	
	public boolean drawFuturePath(int x, int y, int blockWidth, int blockHeight){
		
		path = new int[blocks.length][blocks[0].length];
		
		x /= 10;
		y /= 10;
		int xs = (x - (blockWidth/2))-1;
		int xe = (x + (blockWidth/2))+1;
		int ys = (y - (blockHeight/2))-1;
		int ye = (y + (blockHeight/2))+1;
		
		try{
		
			if(!isWalkable(x*10, y*10, blockWidth, blockHeight)){
				System.err.println("Blocked Path");
				return false;
			}
			
		//block spawns
			for(int i = xs; i < xe; i++){
				path[i][ys] = 1;
				path[i][ye] = 1;
			}
			for(int i = ys; i < ye; i++){
				path[xs][i] = 1;	
				path[xe][i] = 1;	
			}
			path[xe][ye] = 1;
		//Building Path
			for(int i = xs+1; i < xe; i++){
				for(int j = ys+1; j < ye; j++)
					path[i][j] = 2;
			}
			
		}catch(Exception e){System.err.println("Cannot place here");
		return false;}
		
		return true;
		
	}
	
	public boolean isWalkable(int x, int y, int width, int height){

		x /= 10;
		y /= 10;
		int xs = (x - (width/2));
		int xe = (x + (width/2))+1;
		int ys = (y - (height/2));
		int ye = (y + (height/2))+1;
		
		for(int i = xs; i < xe; i++){
			for(int j = ys; j < ye; j++){
				if(blocks[i][j] != 0 && blocks[i][j] != 1)
					return false;
			}
		}
		
		return true;
		
	}

	public void updateBlocks() {
		
		//clear
		for(int i = 0; i < blocks.length; i++){
			for(int j = 0; j < blocks[i].length; j++)
				blocks[i][j] = 0;
		}
		
		for(int i = 0; i < state.getUnitSize(); i++){
			
			if(state.getUnit(i) instanceof Troop)
				break;
			Building b = (Building) state.getUnit(i);
			
			blockSpawns(b.getLocation(), b.getWidth(), b.getHeight());
			
		}
		for(int i = 0; i < state.getUnitSize(); i++){
			
			if(state.getUnit(i) instanceof Troop)
				break;
			Building b = (Building) state.getUnit(i);
			
			block(b.getLocation(), b.getID(), b.getWidth(), b.getHeight());
			
		}

	}

	private void blockSpawns(Point2D location, int width, int height) {

		int centerx = (int) (location.getX()/10);
		int centery = (int) (location.getY()/10);

		int lx = centerx - (width/10/2) - 1;
		int rx = centerx + (width/10/2) + 1;
		int ty = centery - (height/10/2) - 1;
		int by = centery + (height/10/2) + 1;
		
		for(int i = lx; i < rx; i++){
			blocks[i][ty] = 1;
			blocks[i][by] = 1;
		}
		for(int i = ty; i < by; i++){
			blocks[lx][i] = 1;	
			blocks[rx][i] = 1;	
		}
		blocks[rx][by] = 1;
		
	}
	
//[Unit	Utilities]==============================================
	
	public Unit findUnit(int uid){
		return state.find(uid);
	}
	
	public Unit findTarget(Point2D location, int owner, int priority, int uid){
		return state.find(location, owner, priority, uid);
	}
	
	public void cancelTarget(int id){
		state.stopFollowers(id);
	}
	public void add(int type, Point2D location, int index, boolean build) {
		state.addUnit(type, location, index==-1?this.system.getIndex():index, build);
	}
	public void kill(int id) {
		state.remove(id);
	}
	public void deleteBuilding(Building build){
		if(build != null){
			state.removeBuilding(build);
			updateBlocks();
		}
	}
	
//[Graphics]====================================================
	
	public void drawGrid(Graphics g){
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		for(int i = 0; i < this.getWidth(); i+=10)
			for(int j = 0; j < this.getHeight(); j+=10){
				if(i%100 == 0 && j%100 == 0)
					g.drawString(i+","+j, i, j+20);
				
				if(i%10 == 0){
					if(j%100 == 0)
						g.setColor(Color.BLUE);
					g.drawLine(i, j, i+10, j);
				}
				g.setColor(Color.LIGHT_GRAY);
				if(j%10 == 0){
					if(i%100 == 0)
						g.setColor(Color.BLUE);
					g.drawLine(i, j, i, j+10);
				}
				g.setColor(Color.LIGHT_GRAY);
				
				
			}
		
	}
	
	public void showAllPaths(Graphics g){
		
		for(int i = 0; i < this.getWidth(); i+=10)
			for(int j = 0; j < this.getHeight(); j+=10){
				if(blocks[i/10][j/10] == 1 ){
					g.setColor(Color.green);
					g.drawRect(i, j, 10, 10);
				}
				if(path[i/10][j/10] == 1){
					g.setColor(Color.green);
					g.drawRect(i, j, 10, 10);
				}
				
				if(blocks[i/10][j/10] > 1){
					g.setColor(Color.red);
					g.drawRect(i, j, 10, 10);
				}
				if(path[i/10][j/10] == 2){
					g.setColor(Color.red);
					g.drawRect(i, j, 10, 10);
				}
				
			}
		
	}

	@Override
	public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			//draw map
			g.drawImage(SpriteLoader.maps[index], 0, 0, null);
			//draw grid
			drawGrid(g);
			//draw units
			for(int i = 0; i < state.getUnitSize(); i++){
				state.getUnit(i).draw(g);
			}
			
				showAllPaths(g);
	}

//[Threads]==========================================================	
	
	@Override
	public void run() {
		
		while(true){
			
			try {
				Thread.sleep(100);
				for(int i = 0; i < this.state.getUnitSize(); i++){
					try{
						this.state.getUnit(i).run();
					}
					catch(Exception e){e.printStackTrace();}
				}
	//			if(system != null && system instanceof backend.system.Client)
					this.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
//[Client/Server]==========================================	
	
	public void addServer(int type, Point2D p) {
		Client sys = (Client) system;
		sys.sendServer(type, p, index);
	}

	public void addServerB(int type, Point2D arbitrary) {
		Client sys = (Client) system;
		sys.sendServerB(type, arbitrary, index);	
	}
	
	
	
}
