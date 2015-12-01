package frontend.game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import backend.game.system.State;
import backend.system.GSystem;
import backend.system.Address;
import backend.system.Client;

public class GameScreen extends JPanel {

	private static final long serialVersionUID = 1L;
	private GSystem system;
	private MapPanel[] panels;
	private int playersCount;
    private ChatPanel chatbox;
	
	public GameScreen(GSystem sys, boolean isServer){
		
		system = sys;
		
		if(isServer)
			this.setPreferredSize(new Dimension(800,700));
		else
			this.setPreferredSize(new Dimension(800,600));
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		this.playersCount = system.playersCount();
		
		panels = new MapPanel[playersCount];
		for(int i = 0; i < playersCount; i++){
			panels[i] = new MapPanel(i, new ViewPort(system.getMap(i), isServer), isServer, this);
			this.add(panels[i]);
			if(!isServer){
				if(i == system.getIndex())
					panels[i].buildMode = true;
				else{
					panels[i].buildMode = false;
					panels[i].switchTools(1);
					panels[i].setEnabled(false);
				}
			}
		}
		
		this.setBackground(Color.BLACK);
		if(!isServer){
			chatbox = new ChatPanel(((Client) (sys)).getName(), ((Client)sys).getServer());
			if(playersCount == 2){
				chatbox.setPreferredSize(new Dimension(800, 300));
			}else{
				chatbox.setPreferredSize(new Dimension(400, 300));
			}
			
			this.add(chatbox);
		}
	}
	
	//[Testing Purposes]
	public GameScreen(int size){
		this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		
		this.setBackground(Color.BLACK);
		playersCount = size;
		
		try {
			chatbox = new ChatPanel("Tester", new Address(InetAddress.getLocalHost(), 10001));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if(playersCount == 2){
			chatbox.setPreferredSize(new Dimension(800, 300));
			chatbox.setMinimumSize(new Dimension(800, 300));
		}else{
			chatbox.setPreferredSize(new Dimension(400, 300));
		}
		
		panels = new MapPanel[playersCount];
		for(int i = 0; i < playersCount; i++){
			panels[i] = new MapPanel(i, new ViewPort(new Map(system, new State(), i, false), false), false, this);
			this.add(panels[i]);
			panels[i].buildMode = true;
		}
		this.add(chatbox);
		
		
	}
	
	@Override
	public int getWidth(){	
		return 800;
		//return parent.getWidth();
	}
	@Override
	public int getHeight(){	
		return 600;
		//return parent.getHeight();
	}
	
	public static void main(String[] arg){
		
		GameScreen p = new GameScreen(2);
		JFrame main = new JFrame("Test");
		p.setPreferredSize(new Dimension(800,600));
		main.setContentPane(p);
		main.pack();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
		
	}
	
	public void battle(){
		for(int i = 0; i < playersCount; i++){
			panels[i].setVisible(false);
			panels[i].buildMode = false;
			panels[i].setEnabled(true);
			panels[i].switchTools(1);
			panels[i].setVisible(true);
		}
	}

	public void ready() {
		chatbox.ready();
	}
	
}
