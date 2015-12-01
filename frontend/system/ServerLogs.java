package frontend.system;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import backend.system.Server;

public class ServerLogs extends JFrame implements KeyListener {

	private JPanel logPane;
	private JTextArea logs;
	private JTextField address;
	private JTextField limiter;
	private Server system;
	
	public ServerLogs(Server sys){
		
		super("Server");
		
		system = sys;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		logPane = new JPanel();
		logPane.setBackground(Color.white);
		logPane.setPreferredSize(new Dimension(800,600));
		
		logs = new JTextArea();
		logs.setEditable(false);
		JScrollPane sp = new JScrollPane(logs);
		sp.setPreferredSize(new Dimension(750,520));
		logs.setBackground(new Color(250,250,250));
		sp.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		address = new JTextField();
		address.setText("Unknown Host Address");
		address.setEditable(false);
		
		limiter = new JTextField(3);
		limiter.setText("10");
		limiter.addKeyListener(this);
		
		logPane.add(address);
		logPane.add(sp);
		logPane.add(new JLabel("Packet Limiter:"));
		logPane.add(limiter);
		
		this.setContentPane(logPane);
		this.pack();
		this.setVisible(true);
		
	}
	
	public void post(String log){
		
		Date now = new Date();
		logs.append("["+DateFormat.getDateTimeInstance().format(now).toString()+"] - " + log + "\n");
		
	}
	
	public static void main(String[] args) {

		ServerLogs sl = new ServerLogs(null);
		sl.post("Connected!");
		
	}
	
	public void setAddress(String add){
		address.setText(add);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getSource() == limiter){
			
			try{
				system.setLimiter(Integer.parseInt(limiter.getText()));
			}catch(Exception ex){ex.printStackTrace();}
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
}
