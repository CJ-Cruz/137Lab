package frontend.game;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import backend.system.Client;
import backend.system.GSystem;
import backend.system.Server;

public class GameWindow extends JFrame {

	private GSystem user;
	private JPanel buildPane;
	private CardLayout cards;
	private JPanel canvas;
	private GameScreen gs;
	private boolean isServer;
	
	private void initialize(){
	
		if(!isServer)
			this.setTitle(((Client) user).getName());
		
		cards = new CardLayout();
		canvas = new JPanel();
		canvas.setLayout(cards);
		if(isServer){
			if(user.playersCount() > 2)
				canvas.setPreferredSize(new Dimension(700,700));
			else
				canvas.setPreferredSize(new Dimension(700,350));
			this.setTitle("Server");
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.white);
		
		buildPane = new JPanel();
		buildPane.setBackground(new Color(123,231,111));
		buildPane.add(new JLabel("Build Mode"));
		
		gs = new GameScreen(user, isServer);
		
		canvas.add(gs, "game");
		cards.show(canvas, "game");
		this.setContentPane(canvas);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	public GameWindow(GSystem u){
		
		user = u;
		isServer = u instanceof Server;
		initialize();
		
	}
	
	public GameWindow(){
		
		initialize();
		
	}
	
	public static void main(String[] args){
		
		GameWindow gw = new GameWindow();
		
	}

	public void fightMode() {
		
		this.gs.battle();
		
	}
	
}
