package backend.system;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Random;

import javax.swing.JFrame;

import backend.game.objects.units.Unit;
import backend.game.objects.units.troops.Barbarian;
import frontend.game.GameWindow;
import frontend.game.Map;
import frontend.title.TitleFrame;

public class Client extends GSystem{

	private GameWindow gw;
	private Address server;
	private String name;
	private TitleFrame tfr;
	private boolean MMOn = false;
	
	public Client(Address add, String n, TitleFrame tf) throws Exception{
		
		tfr = tf;
		name = n;
		server = add;
		status = PLAYER_CONNECTING;
		try{
			initialize();
		}catch(Exception e){
			
			System.out.println("Error! Retrying...");
			try {
				initialize();
			} catch (SocketTimeoutException ste) {
				System.out.println("No server started at " + server.toString());
				System.exit(1);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
		
		socket.setSoTimeout(2500);	//minimize waiting time
		
		Thread t = new Thread(this);
		t.start();
		
	}
	
	private void initialize() throws Exception{
		
		Random r = new Random(System.currentTimeMillis());
		socket = new DatagramSocket(r.nextInt(65000-1024)+1024);
		socket.setSoTimeout(5000);	//timeout error after 5 seconds
		System.out.println("Trying to connect to " + server.toString());
		
		connect();
		
	}
	
	private void connect() throws IOException{
		
		byte[] data = "[CON]".getBytes();
		send(data);
		System.out.println("Sent [CON]");
		String reply = waitReceive();
		if(reply.startsWith("[CON]")){
			
			String[] tok = reply.split("\\|");
			index = Integer.parseInt(tok[1]);
			players = new Player[Integer.parseInt(tok[2])];
			System.out.println("Received Indexing!");
			
			data = ("[OK]|"+name).getBytes();
			send(data);
			
		}
		
	}
	
	private void send(byte[] data) throws IOException{
		
		DatagramPacket p = new DatagramPacket(data, 0, data.length, server.getAdd(), server.getPort());
		socket.send(p);
		
	}

	@Override
	public void run(){
		try{
			
			//Start
			status = PLAYER_CONNECTING;
			while(status == PLAYER_CONNECTING)
				waitConnection();
			
			while(true){
			
				System.out.println("WaitUpdate");
				
				String data = "";
				try {
					data = waitReceive();
					System.out.println("Received: " + data);
				} catch (IOException e) {
					System.err.println("Disconnected from server. Server Timed out.");
					System.exit(1);
				}
				
				if(data.startsWith("[FIGHT]")){
					
					gw.fightMode();
					System.out.println("Fight Mode begin!");
					continue;
					
				}else if(data.startsWith("[MMS]")){
					
					MMOn = true;
					server = new Address(packet.getAddress(), packet.getPort());
					continue;
					
				}else if(data.startsWith("[LT]")){
					//Load Tester
					Random r = new Random();
					String temp = data;
					Thread loadtester = new Thread(new Runnable(){
						
						@Override
						public void run() {
								
							boolean limited = false;
							String scount = temp;
							int count = 0;
							try{
								scount = scount.split("\\|")[1];
								count = Integer.parseInt(scount);
								limited = true;
							}catch(Exception e){}
							
							while(true){
						
								if(limited && count == 0)
									break;
								
								Point2D loc = new Point();
								loc.setLocation(r.nextInt(340)+5, r.nextInt(340)+5);
								
								sendServer(r.nextInt(3)+1, loc, r.nextInt(2));
								
								count--;
								
							}
							
						}
						
					});
					
					loadtester.start();
					
				}
				
				switch(status){
	
				//No need anymore
//				case PLAYER_BASE_SETUP:
//					updateMaps(data);
//					break;
//				
				case PLAYER_PLAYING:
					updateMaps(data);
					break;
					
				}
				
			}
		}catch(Exception ste){
			ste.printStackTrace();
		}
		
	}
	
	private void waitConnection() throws IOException{
		
		while(true){
			System.out.println("WaitCon");
			if(status != PLAYER_CONNECTING)
				break;
			String received = waitReceive();
			String[] rdata = received.split("\\|");
			switch(rdata[0]){
			
			case "[UPDATE]":
				for(int i = 1; i < rdata.length; i++){
					
					String[] pdata = rdata[i].split("\\+");
					players[i-1] = new Player(new Address(Address.findAddress(pdata[0]), Integer.parseInt(pdata[1])), pdata[2]);
					System.out.println("<Update>");
					
				}
				tfr.updateConnect(players);
				
				break;
			case "[GAME]":
				status = PLAYER_PLAYING; //TODO: Change this to PLAYER_BASE_SETUP
				System.out.println("<Game Start>");
				
				tfr.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				tfr.dispose();
				
				maps = new Map[players.length];
				for(int i = 0; i < players.length; i++){
					maps[i] = new Map(this, players[i].getState(), i, index==i);
				}
				
				gw = new GameWindow(this);
				
				break;
			}
			
		}
		
	}
	
	public void updateServer(String unit){
		
		String out = "";
		
	}
	
	private void updateMaps(String allData){
		
		System.out.println(allData);
		
		String[] adata = allData.split("\\|");
		
		if(allData.startsWith("[S]")){
			String[] maps = allData.split("M");
			for(int i = 1; i < maps.length; i++){
				players[i-1].getState().update(maps[i]);
			}
		}
		
	}

	public void sendServer(int type, Point2D p, int index) {
		
		String comp = "[C]|"+this.index+"|"+type+"|"+(int)p.getX()+"|"+(int)p.getY()+"|"+index;
		byte[] data = comp.getBytes();
		try {
			send(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getName() {
		return name;
	}

	public Address getServer() {
		return this.server;
	}

	public void sendServerB(int type, Point2D arbitrary, int index) {
		
		String comp = "[C]|"+this.index+"|"+type+"|"+(int)arbitrary.getX()+"|"+(int)arbitrary.getY()+"|"+index;
		byte[] data = comp.getBytes();
		try {
			send(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void ready() {
		
		System.out.println("Sending server ready code");
		
		try {
			send(("[RDY]|"+this.index).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
