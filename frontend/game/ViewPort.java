package frontend.game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.JViewport;

import backend.game.objects.units.troops.Barbarian;
import backend.system.Client;
import utils.Globals;

public class ViewPort extends JViewport implements MouseListener, MouseMotionListener, MouseWheelListener, Globals{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point location = new Point(0,0);
	private Point dragStart = new Point();
	private Point tempLocation = new Point();
	private boolean dragOn = false;
	private float zoom = 1f;
	private MapPanel panel;
	private boolean buildable;
	
	public ViewPort(JPanel view, boolean server){
		
		this.setView(view);
		if(!server){
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.addMouseWheelListener(this);
			this.setPreferredSize(new Dimension(285,290));
		}
		else
			this.setPreferredSize(new Dimension(350,350));
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}
	
	private void sendUnit(MouseEvent e, Map m){
		
		if(!m.isBlocked((int)((location.getX()+e.getPoint().getX())/10), (int)((location.getY()+e.getPoint().getY())/10))){
			int type = panel.getSelection();
			Point2D p = new Point();
			p.setLocation(location.getX()+e.getPoint().getX(), location.getY()+e.getPoint().getY());
			if(m.getSystem()!=null)
				m.addServer(type, p);
		}
		
	}

	private void sendBuilding(MouseEvent e, Map m){
		
		Point2D arbitrary = new Point();
		arbitrary.setLocation(location.getX()+e.getPoint().getX(), location.getY()+e.getPoint().getY());
		int type = panel.getSelection();
		if(m.isWalkable((int) arbitrary.getX(), (int) arbitrary.getY(), BLOCK_WIDTH[type], BLOCK_HEIGHT[type])){
			arbitrary.setLocation(Math.floor(arbitrary.getX()/10)*10, Math.floor(arbitrary.getY()/10)*10);
			m.addServerB(type, arbitrary);
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if(this.isEnabled())
		if(e.getButton() == MouseEvent.BUTTON3){
			tempLocation = location;
			this.dragStart = e.getPoint();
			System.out.println("Viewport "+e.getLocationOnScreen().toString()+"-Pressed!");
			setDrag(true);
		}else if(e.getButton() == MouseEvent.BUTTON1){
			System.out.println("Left Pressed!");
			Map m = (Map) this.getView();
			
			if(panel.isBuildMode()){
				
				if(buildable){
					sendBuilding(e, m);
				}
				
			}else sendUnit(e, m);
			
			
			this.repaint();
		}
			
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(e.getButton() == MouseEvent.BUTTON3){
			System.out.println("Viewport "+e.getLocationOnScreen().toString()+"-Released!");
			setDrag(false);
			
			if(tempLocation != location){
				//Drag ViewPort
				this.location.setLocation(tempLocation);
			}else{
				//Delete Building [Unimplemented in Network]
//				Map m = (Map) this.getView();
//				Point2D arbitrary = new Point();
//				arbitrary.setLocation(location.getX()+e.getPoint().getX(), location.getY()+e.getPoint().getY());
//				m.deleteBuilding(m.getBuildingAt((int)arbitrary.getX()/10, (int)arbitrary.getY()/10));
			}
		}
	}
	
	public void setDrag(boolean d){
		this.dragOn = d;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		if(dragOn){
			System.out.println("Dragged to "+e.getPoint().toString()+"!");

			int xDelta = (int)(this.dragStart.getX()-e.getPoint().getX());
			int yDelta = (int)(this.dragStart.getY()-e.getPoint().getY());
			
			this.drag(xDelta, yDelta);
			
		}
			
	}
	
	public void drag(int xd, int yd){

		System.out.println(this.getPreferredSize());
		
		int h = (int) (this.getView().getSize().getHeight()-this.getPreferredSize().getHeight());
		int w = (int) (this.getView().getSize().getWidth()-this.getPreferredSize().getWidth());
		System.out.println(h +","+ w);
		
		if((this.location.getX()+xd > 0 && this.location.getX()+xd < w) && (this.location.getY()+yd > 0 && this.location.getY()+yd < h)){
			//Normal scroll on middle
			tempLocation.setLocation((int)this.location.getX()+xd, (int)this.location.getY()+yd);
			this.setViewPosition(tempLocation);
			this.repaint();
			
		}else if((this.location.getX()+xd <= 0) && (this.location.getY()+yd > 0 && this.location.getY()+yd < h)){
			//Allows horizontal Scroll when on X left edge.	
			System.out.println("XBound!");
			tempLocation.setLocation(0, (int)this.location.getY()+yd);
			
			this.setViewPosition(tempLocation);
			this.repaint();
		
		}else if((this.location.getX()+xd >= w) && (this.location.getY()+yd > 0 && this.location.getY()+yd < h)){
			//Allows horizontal Scroll when on X right edge.	
			System.out.println("XBound!");
			tempLocation.setLocation(w, (int)this.location.getY()+yd);
			
			this.setViewPosition(tempLocation);
			this.repaint();
			
		}else if((this.location.getY()+yd <= 0) && (this.location.getX()+xd > 0 && this.location.getX()+xd < w)){
			//Allows horizontal Scroll when on Y top edge.
			System.out.println("YBound!");
			tempLocation.setLocation((int)this.location.getX()+xd, 0);
			
			this.setViewPosition(tempLocation);
			this.repaint();
		
		}else if((this.location.getY()+yd >= h) && (this.location.getX()+xd > 0 && this.location.getX()+xd < w)){
		//Allows horizontal Scroll when on Y bottom edge.
			System.out.println("YBound!");
			tempLocation.setLocation((int)this.location.getX()+xd, h);
			
			this.setViewPosition(tempLocation);
			this.repaint();
			
		}
		System.out.println("Viewport Location in main: " + tempLocation);
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		if(panel.isBuildMode()){
			Map m = (Map) this.getView();
			Point2D arbitrary = new Point();
			arbitrary.setLocation(location.getX()+e.getPoint().getX(), location.getY()+e.getPoint().getY());
			int type = panel.getSelection();
			if(m.drawFuturePath((int) arbitrary.getX(), (int) arbitrary.getY(), BLOCK_WIDTH[type], BLOCK_HEIGHT[type]))
				buildable = true;
			else buildable = false;
		
		}
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		int scrolls = e.getWheelRotation();
		
		if(scrolls < 0){
			
			zoom += 0.1;
			System.out.println("Scroll up!" + zoom);
			
		}else if(scrolls > 0){
			
			if(zoom-0.1 >= 0.1)
				zoom -= 0.1;
			System.out.println("Scroll down!" + zoom);
			
		}
		
	}

	public void setControls(MapPanel mapPanel) {
		this.panel = mapPanel;
	}

	public Client getSystem(){
		return (Client)((Map)this.getView()).getSystem();
	}
	
}
