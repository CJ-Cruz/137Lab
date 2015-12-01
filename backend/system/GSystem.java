package backend.system;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.LinkedList;

import frontend.game.Map;
import utils.Globals;

public abstract class GSystem implements Runnable, Globals{

	protected DatagramSocket socket;
	protected DatagramPacket packet;
	protected int index;				//game index
	protected Player[] players;
	protected Map[] maps;
	protected int status;
	protected LinkedList<Socket> chatConnections = new LinkedList<Socket>();
	
	//Waits for packet, creates a keep-alive when timed out
	protected String waitReceive() throws IOException{
		
		do{
			byte[] buffer = new byte[65000];
			packet = new DatagramPacket(buffer, buffer.length);
			
			socket.receive(packet);
			
			
		}while(new String(packet.getData()).trim().startsWith("[C]"));
		
		System.out.println("Received: " + new String(packet.getData()).trim());
		return new String(packet.getData()).trim();
		
	}
	
	@Override
	abstract public void run();
	
	public Map getMap(int i){
		return maps[i];
	}

	public int playersCount() {
		return players.length;
	}
	
	public int getIndex() {
		return index;
	}
	
}
