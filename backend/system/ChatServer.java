
package backend.system;

import java.net.*;
import java.util.ArrayList;

public class ChatServer implements Runnable{
    
    static int port = 51138;
    Server system;
    
    public ChatServer(Server sys){
    
    	system = sys;
        
    	Thread tcs = new Thread(this);
        tcs.start();
        
    }
    
    @Override
    public void run(){
        try{
            ServerSocket server = new ServerSocket(port);
            
            while(true){
                Socket sock = server.accept();
                system.chatConnections.add(sock);
                
                ServerResponse sr = new ServerResponse(sock, system);
                Thread t = new Thread(sr);
                t.start();
            }
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public static void main(String[] args) throws Exception{
        try{
            
            ServerSocket server = new ServerSocket(port);
            
            while(true){
                Socket socket = server.accept();
                
//                ServerResponse sr = new ServerResponse(socket, system);
//                Thread t = new Thread(sr);
//                t.start();
            }
        }catch(Exception e){System.out.println(e);}
        
    }
}
