package frontend.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Toolbox extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private MapPanel panel;
	private GameScreen gs;
	
	public Toolbox(int toolset, GameScreen g){
		
		gs = g;
		this.setLayout(new GridLayout(3,1));
		this.setPreferredSize(new Dimension(100,150));
		this.setBackground(new Color(240,240,240));
		this.setBorder(BorderFactory.createLineBorder(new Color(250,250,250), 2, true));
		SetTools(toolset);

		set[0][0].addActionListener(this);
		set[0][1].addActionListener(this);
		set[0][2].addActionListener(this);
		set[1][0].addActionListener(this);
		set[1][1].addActionListener(this);
		set[1][2].addActionListener(this);
		
	}
	
	public void setPanel(MapPanel p){
		panel = p;
	}
	
	private JButton[][] set = {
			{new JButton("Building"), new JButton("Wall"), new JButton("Ready")},	//set 0 - buildings
			{new JButton("Barbarian"), new JButton("Archer"), new JButton("Giant")}	//set 1 - units
		};
	
	public void SetTools(int toolset){
		
		this.removeAll();
		
		for(int i = 0; i < set[toolset].length; i++){
			this.add(set[toolset][i]);
		}
		
		this.repaint();
		
	}
	
	@Override
	public void setEnabled(boolean b){
		
		for(int i = 0; i < set.length; i++){
			set[i][0].setEnabled(b);
			set[i][1].setEnabled(b);
			set[i][2].setEnabled(b);
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource() == set[0][0]){
			panel.selection = 1;
		}else if(ae.getSource() == set[0][1]){
			panel.selection = 2;
		}else if(ae.getSource() == set[0][2]){
			
			this.setEnabled(false);
			panel.vp.setEnabled(false);
			panel.vp.getSystem().ready();
			gs.ready();
			
		}else if(ae.getSource() == set[1][0]){
			panel.selection = 1;
		}else if(ae.getSource() == set[1][1]){
			panel.selection = 2;
		}else if(ae.getSource() == set[1][2]){
			panel.selection = 3;
		}
		
		System.out.println(panel.getSelection());
		
	}
	
	public static void main(String[] arg){
		
		Toolbox p = new Toolbox(0, null);
		JFrame main = new JFrame("Test");
		main.setContentPane(p);
		main.pack();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
		
	}
	
}
