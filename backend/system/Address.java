package backend.system;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Address implements Serializable{

	private InetAddress address;
	private int port;
	
	public InetAddress getAdd(){
		return address;
	}
	
	public int getPort(){
		return port;
	}
	
	public Address(InetAddress add, int p){
		this.address = add;
		this.port = p;
	}
	
	public String toString(){
		return address.getHostAddress() + ":" + port;
		
	}
	
	public boolean eqs(Address a){
		
		if(!a.getAdd().equals(this.getAdd()))
			return false;
		if(a.getPort() != this.getPort())
			return false;
		
		return true;
		
	}
	
	public static InetAddress findAddress(String IP) throws UnknownHostException{
		
		String[] bytes = IP.split("\\.");
		
		byte[] data = new byte[4];
		for(int i = 0; i < 4; i++){
			data[i] = (byte) Integer.parseInt(bytes[i]);
		}
		
		return InetAddress.getByAddress(data);
		
	}
	
}
