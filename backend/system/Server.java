package backend.system;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

import javax.swing.JFrame;

import frontend.game.GameWindow;
import frontend.game.Map;
import frontend.system.ServerLogs;
import utils.Convert;

public class Server extends GSystem{

	private ServerLogs log;
	private int quantum;
//	private ServerMaps server;
	private boolean intervalSend;
	private GameWindow neoServer;
	private boolean MMOn;
	private Address MM;
	private int limiter = 10;
	private LinkedList<String> requests = new LinkedList<String>();
	private LinkedList<DatagramPacket> rpackets = new LinkedList<DatagramPacket>();
	private ChatServer chatserv;
	
	public Server(int playersCount) throws SocketException{
		
		index = -1;
		System.out.println(playersCount);
		socket = new DatagramSocket(39585);
		players = new Player[playersCount];
		log = new ServerLogs(this);
//		server = new ServerMaps(this);
		
		chatserv = new ChatServer(this);
		
		try{
			log.setAddress(InetAddress.getLocalHost().getHostAddress() + ":" + 39585);
		}catch(Exception e){
			System.err.println("Cannot place correct IP Address");
		}
	
		Thread t = new Thread(this);
		t.start();
		
		Thread processor = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if(!requests.isEmpty()){
						
						String request = requests.removeFirst();
						
						if(request.isEmpty()){
							try {
								rpackets.removeFirst();
								broadcast("[C]");
							} catch (IOException e) {
								e.printStackTrace();
							}
							continue;
						}
						
						switch(status){
							
							//Build Mode
							case SERVER_STARTING:
								if(request.startsWith("[RDY]")){
									System.out.println(request);
									DatagramPacket p = rpackets.removeFirst();
									Address u = new Address(p.getAddress(), p.getPort());
									players[Integer.parseInt(request.split("\\|")[1])].setStatus(2);
									
									boolean checkready = true;
									for(int i = 0; i < playersCount; i++){
										if(players[i].getStatus() != 2)
											checkready = false;
									}
									
									if(checkready){
										status = SERVER_PLAYING;
										try {
											broadcast("[FIGHT]");
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
								else
									receiveBuilding(request, rpackets.removeFirst());
								break;
							
							case SERVER_PLAYING:
								receiveUnit(request, rpackets.removeFirst());
								break;
								
							}
					}
				}
			}

			
		});
		processor.start();
		
	}
	
	private void receive() throws IOException{
		
		byte[] buffer = new byte[2048];
		DatagramPacket p = new DatagramPacket(buffer, buffer.length);
		socket.setSoTimeout(200);
		try{
			socket.receive(p);
		}catch(Exception e){
			//System.out.println("Empty Receive");
		}
		socket.setSoTimeout(0);
		
		String request = new String(p.getData()).trim();

		if(request.startsWith("[MMS]")){
			
			MMOn = true;
			MM = new Address(p.getAddress(), p.getPort());
			
			Address[] clients = new Address[players.length];
			for(int i = 0; i < players.length; i++)
				clients[i] = players[i].getAddress();
			
			byte[] data = Convert.toByteArray(clients);
			
			DatagramPacket pn = new DatagramPacket(data, data.length, MM.getAdd(), MM.getPort());
			try {
				socket.send(pn);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if(requests.size() < limiter){
			requests.addLast(request);
			rpackets.addLast(p);
		}
		else	//don't process more requests, but pulse keepAlive to clients
			broadcast("[C]");
		
	}
	
	@Override
	public void run(){
		try {
			
			//Connection Phase
			completePlayers();
			status = SERVER_STARTING;
			maps = new Map[players.length];
			for(int i = 0; i < players.length; i++)
				maps[i] = new Map(this, players[i].getState(), i, false);
		//	broadcast("[SR]|0|");
		//TODO:	quantum = playersCount();
			neoServer = new GameWindow(this);
			intervalUpdate();
			while(true){
			
				receive();
				
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void intervalUpdate() {
		
		intervalSend = true;
		
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				
				while(intervalSend){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					updateClients();
				}
				
			}
			
			
			
		});
		
	}

	//Connection Phase
	public void completePlayers() throws IOException{
		
		int connected = 0;
		
		while(connected != players.length){
			
			receive();
			String request = requests.removeFirst();
			if(request.isEmpty()||request.equals("[C]")){
				rpackets.removeFirst();
				broadcast("[C]");
			}
			else if(request.startsWith("[CON]")){
				
				String reply = "[CON]|" + connected + "|" + players.length;
				byte[] data = reply.getBytes();
				reply(data, rpackets.removeFirst());
				
				try{
					DatagramPacket p;
					String received;
					while(true){
						receive();
						received = requests.removeFirst();
						p = rpackets.removeFirst();
						if(received.startsWith("[OK]"))
							break;
					}
					String[] rdata = received.split("\\|");
					
					//If client successfully replied
					players[connected] = new Player(new Address(p.getAddress(), p.getPort()), rdata[1]);
					connected++;
					
					//TODO: broadcast names
					reply = "[UPDATE]|";
					for(int i = 0; i < players.length; i++){
						if(players[i] != null)
							reply += players[i].getAddress().getAdd().getHostAddress() + "+" + players[i].getPort() + "+" + players[i].getName() + "|";
						
					}
					broadcast(reply);
					System.out.println("Sent players updates!");
					
				}catch(SocketTimeoutException ste){
					
					System.err.println("No reply from client");
					
				}
				//remove timeout
				socket.setSoTimeout(0);
			}
			
		}
		
		System.out.println("All players Connected! Commencing Game...");
		broadcast("[GAME]");
		
	}
	
	private void reply(byte[] data, DatagramPacket p) throws IOException{
		
		p = new DatagramPacket(data, data.length, p.getAddress(), p.getPort());
		socket.send(p);
		System.out.println("Sent {" + new String(data).trim() + "} to " + findName(new Address(p.getAddress(), p.getPort())));
		
	}
	
	public void broadcast(String data) throws IOException{
		
		if(!data.equals("[C]"))
			log.post("Broadcasting {" + data + "}...");
		
		byte[] d = data.getBytes();
		for(int i = 0; i < players.length; i++){
			if(players[i] != null){
				
				if(!MMOn)
					packet = new DatagramPacket(d, d.length, players[i].getIP(), players[i].getPort());
				else
					packet = new DatagramPacket(d, d.length, MM.getAdd(), MM.getPort());
				
				socket.send(packet);
				if(!data.equals("[C]")){
					log.post("Sent {" + data + "} to " + findName(players[i].getAddress()));
					System.out.println("Sent {" + data + "} to " + findName(players[i].getAddress()));
				}
				
			}
			
		}
	}
	
	private String findName(Address a){
	
		for(int i = 0; i < players.length; i++){
			
			if(players[i] != null){
				
				if(players[i].getAddress().eqs(a)){
					return players[i].getName();
				}
				
			}
			
		}
		
		return a.toString();
		
	}
	
	private void receiveUnit(String mdata, DatagramPacket p){

		log.post("Received Unit Update from ["+findName(new Address(p.getAddress(), p.getPort()))+"]: {"+mdata+"}");
		
		String[] adata = mdata.split("\\|");
		
		int pnum = Integer.parseInt(adata[1]);
		int type = Integer.parseInt(adata[2]);
		int px = Integer.parseInt(adata[3]);
		int py = Integer.parseInt(adata[4]);
		int mdex = Integer.parseInt(adata[5]);
		
		Point2D loc = new Point();
		loc.setLocation(px, py);
		
		maps[mdex].add(type, loc, pnum, false);
		
		updateClients();
		
	}


	private void receiveBuilding(String request, DatagramPacket p) {

		log.post("Received Building Update from ["+findName(new Address(p.getAddress(), p.getPort()))+"]: {"+request+"}");
		
		String[] adata = request.split("\\|");
		
		int pnum = Integer.parseInt(adata[1]);
		int type = Integer.parseInt(adata[2]);
		int px = Integer.parseInt(adata[3]);
		int py = Integer.parseInt(adata[4]);
		int mdex = Integer.parseInt(adata[5]);
		
		Point2D loc = new Point();
		loc.setLocation(px, py);
		
		maps[mdex].add(type, loc, pnum, true);
		
		updateClients();
		
	}
	
	//Deprecated
	private void updateMap(String mdata){
		
		log.post("Received Map Update: {"+mdata+"}");
		
		System.out.println("UpdateMap");
		
		String[] adata = mdata.split("\\|");
		
		//Create rotation of send/receive updates
		int replyIndex = Integer.parseInt(adata[1]);
		replyIndex++;
		
		update(mdata);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		//Compiles recent data
/*		if(replyIndex == playersCount()){
			replyIndex = 0;
	*/		String compiled = compile();
			try {
				broadcast(compiled);
			} catch (IOException e) {
				e.printStackTrace();
			}
	//	}
			
	/*	try {
			broadcast("[SR]|"+replyIndex+"|");
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	private String compile() {
		
		String out = "[S]|";
		
		for(int i = 0; i < playersCount(); i++){
			
			out += this.players[i].getState().compile();
			
		}
		
		return out;
		
	}
	
	private void update(String data){
		
		String[] maps = data.split("M");
		for(int i = 1; i < maps.length; i++){
			players[i-1].getState().update(maps[i]);
		}
		
		System.out.println("Update finished!");
		
	}
	
	public void updateClients(){
		
		String compiled = compile();
		try {
			broadcast(compiled);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setLimiter(int lim){
		limiter = lim;
	}
	
}
