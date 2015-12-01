package backend.system;

import java.net.InetAddress;

import backend.game.system.State;

public class Player {

	private Address address;
	private String name;
	private State state;
	private int status;
	
	public Player(Address a, String n){
		address = a;
		name = n;
		state = new State();
		setStatus(1);
	}

	public InetAddress getIP() {
		return address.getAdd();
	}
	
	public Address getAddress(){
		return address;
	}
	
	public int getPort(){
		return address.getPort();
	}

	public String getName() {
		return name;
	}

	public State getState() {
		return state;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
