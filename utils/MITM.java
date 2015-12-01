package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import backend.system.Address;

public class MITM {

	private Address server;
	private Address[] clients;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private LinkedList<DatagramPacket> packets = new LinkedList<DatagramPacket>();
	private LinkedList<Integer> targets = new LinkedList<Integer>();
	private int playersCount;
	private int current;
	private boolean locked;
	private boolean flushing = false;
	private boolean open;
	private boolean permit;
	private DefaultTableModel out;
	private DefaultTableModel cap;
	
	public MITM(){
		
		try {
			socket = new DatagramSocket(51138);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		String ip = tryConnect(JOptionPane.showInputDialog("Enter IP address of Server"));
//		String ip = "Null";
		MiddleFrame m = new MiddleFrame(ip, this);
		MiddlePane p = (MiddlePane) m.getContentPane();
		out = ((DefaultTableModel) p.outgoing.getModel());
		cap = ((DefaultTableModel) p.captures.getModel());
		while(true){
			
			DatagramPacket tpacket = receive();
			
			String data = new String(tpacket.getData()).trim();
			
			if(!data.equals("[C]"))
				System.out.println(new Address(tpacket.getAddress(), tpacket.getPort())+"> "+data);
			
			if(data.startsWith("[RDY]")){
				send(tpacket, -1);
				locked = false;
			}
				
			if(p.manual){
				
				packets.add(tpacket);
				String src = new Address(tpacket.getAddress(),tpacket.getPort()).toString();
				String dest = "";
				if(new Address(tpacket.getAddress(), tpacket.getPort()).eqs(server)){
					dest = clients[current].toString();
					targets.add(current);
					current++;
					if(current == playersCount)
						current = 0;
				}else{	
					dest = server.toString();
					targets.add(-1);
				}
				
				String[] row = {src, data, dest};
				out.addRow(row);
				out.fireTableDataChanged();
				
			}else{
				if(!data.startsWith("[C]") && new Address(tpacket.getAddress(), tpacket.getPort()).eqs(server)){
					send(tpacket, current);
					current++;
					if(current == playersCount){
						current = 0;
						locked = false;
					}
				}else if(data.startsWith("[C]") && !(new Address(tpacket.getAddress(), tpacket.getPort()).eqs(server))){
					send(tpacket, -1);
					locked = false;
				}
				
			}
			
		}
	
	}
	
	public DatagramPacket getPacket(int index){
		return packets.get(index);
	}
	
	private String tryConnect(String add){
		
		byte[] data = "[MMS]".getBytes();
		byte[] rec = new byte[65000];
		try {
			socket.setSoTimeout(5000);
		
			packet = new DatagramPacket(data, data.length, Address.findAddress(add), 39585);
			socket.send(packet);
			
			packet = new DatagramPacket(rec, rec.length);
			System.out.println("Waiting for response");
			String tester;
			do{
				socket.receive(packet);
				tester = new String(packet.getData());
			}while(tester.startsWith("[C]")||tester.startsWith("[S]"));
			server = new Address(packet.getAddress(), packet.getPort());
			clients = (Address[]) Convert.ByteArrayToObject(packet.getData());
			playersCount = clients.length;
			current = 0;
			
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					for(int i = 0; i < clients.length; i++){
						byte[] data = "[MMS]".getBytes();
						send(new DatagramPacket(data, data.length),current);
						current++;
						if(current == playersCount)
							current = 0;
					}
				}
				
			});
			
			t.start();
			
			socket.setSoTimeout(2500);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("No response/Wrong Response from server!");
			System.exit(0);
			
		}
		
		return add;
		
	}
	
	public void flushPackets() {
		
		while(packets.size() > 0){
			
			System.out.println("Sending to target...");
			
			DatagramPacket p = packets.removeFirst();
			int t = targets.removeFirst();
//			flushing = true;
//			while(!open)System.out.print("");
			send(p, t);
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			permit = true;
			lock();
			
		}
	}
	
	public DatagramPacket receive(){
		
		byte[] data;
		boolean rep;
		DatagramPacket gpacket = null;
		
		do{
			
			rep = false;
			data = new byte[65000];
			gpacket = new DatagramPacket(data, data.length);
			
			open = true;
//			while(flushing){
//				if(permit)
//					break;
//				else{
//					for(int i = 0; i < clients.length; i++)
//						send(new DatagramPacket("[C]".getBytes(), "[C]".getBytes().length), i);
//					try {
//						Thread.sleep(1);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
			
			try {
//				Thread.sleep(1);
//				open = false;
				socket.receive(gpacket);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//			flushing = false;
//			permit = false;
			
			//Automatically Send Connection check to client
			String rdata = new String(gpacket.getData()).trim();
			if(rdata.equals("[C]")){
				rep = true;
				send(gpacket, current);
				current++;
				if(current == playersCount)
					current = 0;
			}
		
		}while(rep);
		
		return gpacket;
		
	}
	
	public void send(DatagramPacket tpacket, int index){
		
		String data = new String(tpacket.getData()).trim();
		String src = "";
		if(!data.equals("[MMS]") && !data.equals("[C]") && !data.startsWith("[LT]"))
			 src = new Address(tpacket.getAddress(), tpacket.getPort()).toString();
		packet = tpacket;
		
		if(index == -1){
			packet.setAddress(server.getAdd());
			packet.setPort(server.getPort());
		}
		else{
			packet.setAddress(clients[index].getAdd());
			packet.setPort(clients[index].getPort());
		}
		
		if(!data.equals("[C]") && !data.equals("[MMS]")){
			String dest = new Address(packet.getAddress(), packet.getPort()).toString();
			String[] row = {src, data, dest};
			cap.addRow(row);
			cap.fireTableDataChanged();
		}
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		
		MITM m = new MITM();
		
	}

	public String getTarget(int index) {
		
		int t = targets.get(index);
		
		if(t == -1) return server.toString();
		
		else{
			
			return clients[t].toString();
			
		}
		
	}

	public void delete(int index) {
		
		packets.remove(index);
		targets.remove(index);
		
	}

	public int findTarget(Address a) {
		
		if(a.eqs(server))
			return -1;
		else{
			
			for(int i = 0; i < clients.length; i++)
				if(clients[i].eqs(a))
					return i;
			
		}
		
		return -2;
		
	}
	
	public Address[] getClients(){
		return clients;
	}

	public void lock() {
		locked = true;
		while(locked)System.out.print("");
	}
	
}

class MiddleFrame extends JFrame{
	
	public MiddleFrame(String ip, MITM sys){
		
		super("Man in the Middle");
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setContentPane(new MiddlePane(ip,sys));
		this.pack();
		this.setVisible(true);
		
	}
	
}

class MiddlePane extends JPanel implements ActionListener, KeyListener, MouseListener{

	MITM system;
	JTable outgoing;
	JTable captures;
	JTextField source;
	JTextField destination;
	JTextField length;
	JTextField offset;
	JTextField target;
	JTextArea data;
	JTextArea log;
	JButton rswitch;
	JButton send;
	JButton ltest;
	boolean manual = false;
	int current = 0;
	
	private GridBagConstraints gbc = new GridBagConstraints();
	
	public MiddlePane(String ip, MITM sys){
		
		super(new GridBagLayout());
		
		system = sys;
		this.setPreferredSize(new Dimension(800,600));
		this.setBackground(Color.white);
		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		this.add(new JLabel("Connected to Server: "+ip), gbc);
		gbc.gridy++;
		this.add(new JLabel(" "), gbc);
		gbc.gridy++;
		
		setupTable();
		
		
		this.add(new JLabel("Source"), gbc);
		gbc.gridy++;
		source = new JTextField();
		source.setPreferredSize(new Dimension(380, 20));
		this.add(source, gbc);
		
		gbc.gridx++;
		gbc.gridy--;
		this.add(new JLabel("Destination"), gbc);
		gbc.gridy++;
		destination = new JTextField();
		destination.setPreferredSize(new Dimension(380, 20));
		this.add(destination, gbc);
		gbc.gridx--;
		gbc.gridy++;
		
		this.add(new JLabel("Length"), gbc);
		gbc.gridy++;
		length = new JTextField();
		length.setPreferredSize(new Dimension(380, 20));
		this.add(length, gbc);
		
		gbc.gridx++;
		gbc.gridy--;
		this.add(new JLabel("Offset"), gbc);
		gbc.gridy++;
		offset = new JTextField();
		offset.setPreferredSize(new Dimension(380, 20));
		this.add(offset, gbc);
		gbc.gridx--;
		gbc.gridy++;

		this.add(new JLabel("Data"), gbc);
		gbc.gridy++;
		gbc.gridwidth = 2;
		data = new JTextArea();
		JScrollPane dsp = new JScrollPane(data);
		dsp.setPreferredSize(new Dimension(760,80));
		this.add(dsp, gbc);
		gbc.gridy++;
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		send = new JButton("Send");
		send.setPreferredSize(new Dimension(100, 50));
		send.addActionListener(this);
		this.add(send, gbc);
		gbc.gridx--;
		gbc.gridy++;
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridwidth = 1;
		ltest = new JButton("Load Test Manipulate");
		ltest.addActionListener(this);
		this.add(ltest, gbc);
		gbc.gridx++;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		this.add(new JLabel("Taget player:"), gbc);
		target = new JTextField(2);
		gbc.gridx++;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		this.add(target, gbc);
		
		source.setEnabled(false);
		destination.setEnabled(false);
		length.setEnabled(false);
		offset.setEnabled(false);
		data.setEnabled(false);
		send.setEnabled(false);
		
	}

	private void setupTable() {
		
		String[] cols = {"SRC", "DATA", "DEST"};
		String[][] data = {};
		
		this.add(new JLabel("Captured Packets"), gbc);
		gbc.gridy++;
		captures = new JTable(new DefaultTableModel(data.clone(), cols.clone()));
		captures.getColumnModel().getColumn(1).setPreferredWidth(550);
		JScrollPane jspc = new JScrollPane(captures);
		jspc.setPreferredSize(new Dimension(760, 100));
		this.add(jspc, gbc);
		gbc.gridy++;
		this.add(new JLabel(" "), gbc);
		gbc.gridy++;
		
		this.add(new JLabel("Caught Packets"), gbc);
		gbc.gridy++;
		outgoing = new JTable(new DefaultTableModel(data, cols));
		outgoing.getColumnModel().getColumn(1).setPreferredWidth(550);
		outgoing.addMouseListener(this);
		JScrollPane jsp = new JScrollPane(outgoing);
		jsp.setPreferredSize(new Dimension(760, 100));
		this.add(jsp, gbc);
		gbc.gridy++;


		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		rswitch = new JButton("Pause");
		rswitch.addActionListener(this);
		rswitch.setPreferredSize(new Dimension(400, 30));
		this.add(rswitch, gbc);
		gbc.gridy++;
		this.add(new JLabel(" "), gbc);
		gbc.gridy++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == rswitch){
			
			manual =! manual;
			
			if(manual){
				rswitch.setText("Resume");
				
				outgoing.setEnabled(true);
				source.setEnabled(true);
				destination.setEnabled(true);
				length.setEnabled(true);
				offset.setEnabled(true);
				data.setEnabled(true);
				send.setEnabled(true);
			}else{
				rswitch.setText("Pause");
				
				DefaultTableModel d = ((DefaultTableModel) outgoing.getModel());
				for(int i = d.getRowCount()-1; i >= 0; i--){
					d.removeRow(i);
				}
				d.fireTableDataChanged();
				
				system.flushPackets();
				
				source.setText("");
				destination.setText("");
				length.setText("");
				offset.setText("");
				data.setText("");
				outgoing.setEnabled(false);
				source.setEnabled(false);
				destination.setEnabled(false);
				length.setEnabled(false);
				offset.setEnabled(false);
				data.setEnabled(false);
				send.setEnabled(false);
			}
			
		}
		else if(e.getSource() == this.send){
			
			DefaultTableModel m = (DefaultTableModel) outgoing.getModel();
			m.removeRow(current);
			m.fireTableDataChanged();
			
			String[] addr = source.getText().split(":");
			String add = addr[0];
			int port = Integer.parseInt(addr[1]);
			InetAddress a = null;
			try {
				a = Address.findAddress(add);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			
			DatagramPacket p = new DatagramPacket(data.getText().getBytes(), 
					Integer.parseInt(offset.getText()), 
					Integer.parseInt(length.getText()),
					a,
					port);
			
			addr = destination.getText().split(":");
			String dest = addr[0];
			int dport = Integer.parseInt(addr[1]);
			InetAddress d = null;
			try {
				d = Address.findAddress(dest);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			
			int target = system.findTarget(new Address(d, dport));
			system.delete(current);
			
			system.send(p, target);
			
		}else if(e.getSource() == ltest){
			
			try{
				int pnum = 1;
				String d = "[LT]";
				try{
					String[] tok = target.getText().split("\\|");
					pnum = Integer.parseInt(tok[0]);
					System.out.println(pnum);
					d += "|" + tok[1];
				}catch(Exception e1){e1.printStackTrace();};
				byte[] data = d.getBytes();
				system.send(new DatagramPacket(data, data.length), pnum-1);
			}catch(Exception ex){ex.printStackTrace();};
		}
		
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(manual && e.getSource() == outgoing){

			current = outgoing.getSelectedRow();
			System.err.println(current);
			setData(system.getPacket(current));
			
		}
		
	}

	private void setData(DatagramPacket p) {
		
		this.source.setText(new Address(p.getAddress(), p.getPort()).toString());
		this.destination.setText(system.getTarget(current));
		this.length.setText(String.valueOf(p.getLength()));
		this.offset.setText(String.valueOf(p.getOffset()));
		this.data.setText(new String(p.getData()).trim());
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
}
