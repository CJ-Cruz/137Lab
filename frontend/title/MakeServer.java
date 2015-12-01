package frontend.title;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import backend.system.Server;

public class MakeServer extends JPanel implements ActionListener{

	private JPanel options;
	
	private JFrame par;
	private JButton start;
	private JRadioButton twoP;
	private JRadioButton threeP;
	
	public MakeServer(JFrame p){
		par = p;
		options = new JPanel();
		options.setLayout(new FlowLayout(FlowLayout.LEADING));
		options.setBackground(Color.white);
		
		start = new JButton("Start Game");
		start.addActionListener(this);
		options.add(start);
		options.setPreferredSize(new Dimension(350, 230));
		
		twoP = new JRadioButton("One vs One", true);
		twoP.setBackground(Color.white);
		twoP.addActionListener(this);
		threeP = new JRadioButton("Triple Threat", false);
		threeP.setBackground(Color.white);
		threeP.addActionListener(this);
		options.add(new JLabel("  Mode: "));
		options.add(twoP);
		options.add(threeP);
		
		JScrollPane sc = new JScrollPane(options);
		sc.setPreferredSize(new Dimension(370, 250));
		this.add(sc);
		
	}
	
	public static void main(String[] arg){
		
		JFrame t = new JFrame("tester");
		t.setPreferredSize(new Dimension(400,300));
		t.setContentPane(new MakeServer(t));
		t.pack();
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == start){
			par.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			par.dispose();
			try {
				Server s = new Server(twoP.isSelected()?2:3);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == twoP){
			threeP.setSelected(false);
		}
		else if(e.getSource() == threeP){
			twoP.setSelected(false);
		}
		
	}
	
	public void foc(){

		start.requestFocus();
		
	}
	
}
