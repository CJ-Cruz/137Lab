/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.system;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Melvin
 */
public class ServerResponse implements Runnable{
    
    Socket sock;
    GSystem server;

    public ServerResponse(Socket sock, Server server){
        this.sock = sock;
        this.server = server;
    }
    
    @Override
    public void run() {
        try{
            try{
                Scanner in = new Scanner(sock.getInputStream());
                String msg = "";
                
                while(true){
                    
                    if(!in.hasNext())
                        continue;
                    
                    msg = in.nextLine();
                    
                    System.out.println("Client message: " + msg);
                    
                    for (Socket temp_socket : server.chatConnections) {
                        PrintWriter temp_out = new PrintWriter(temp_socket.getOutputStream());
                        temp_out.println(msg);
                        temp_out.flush();
                        
                        System.out.println("Sent to: " + temp_socket.getLocalAddress().getHostName());
                    }
                }
            }finally{
                sock.close();
            }
        }catch(Exception e){
            System.out.print(e);
        }
    }
    
}
