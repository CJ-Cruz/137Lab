package frontend.game;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import backend.game.objects.units.res.SpriteLoader;
import backend.game.system.State;

public class MapPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	protected int owner;
	protected int selection = 1;
	protected boolean buildMode;
	protected Toolbox tools;
	protected ViewPort vp;
	
	public MapPanel(int index, ViewPort vp, boolean isServer, GameScreen gs){
		
		try {
			new SpriteLoader();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.vp = vp;
		this.owner = index;
		this.setBackground(Color.white);
		if(!isServer){
			tools = new Toolbox(0, gs);
			tools.setPanel(this);
			this.add(tools);
			this.setPreferredSize(new Dimension(400,300));
		}else
			this.setPreferredSize(new Dimension(350,350));
		this.add(vp);
		this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		vp.setControls(this);
	}

//Getter and Setter	
	
	public int getOwner() {
		return owner;
	}
	
	public void setOwner(int o){
		owner = o;
	}
	
	public static void main(String[] arg){
		
		JFrame fr = new JFrame("Map Panel");
		fr.setContentPane(new MapPanel(0, new ViewPort(new Map(null, new State(), 0, false), false), false, null));
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.pack();
		fr.setVisible(true);
		
	}

	public int getSelection() {
		return selection;
	}
	
	public boolean isBuildMode(){
		return buildMode;
	}
	
	public void setModeBuild(boolean build){
		this.buildMode = build;
	}
	
	public void switchTools(int toolkit){
		tools.SetTools(toolkit);
		tools.repaint();
	}
	
	@Override
	public void setEnabled(boolean b){
		
		tools.setEnabled(b);
		vp.setEnabled(b);
	
	}
	
}
