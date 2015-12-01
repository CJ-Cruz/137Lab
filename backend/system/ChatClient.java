/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.system;

import frontend.game.ChatPanel;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Melvin
 */
public class ChatClient implements Runnable{
    
    Socket socket;
    Scanner in;
    PrintWriter out;
    
    public ChatClient(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public void run(){
        try{
            try{
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream());
                while(true){
                    receive();
                }
            }finally{
                socket.close();
            }
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    
//    public void disconnect() throws IOException{
//        socket.close();
//        System.exit(0);
//    }
    
    public void receive(){
        if(in.hasNext()){
            String msg = in.nextLine();
            ChatPanel.msgArea.append(msg + "\n");
        }
    }
    
    public void send(String str){
        out.println(str);
        out.flush();
        ChatPanel.msgBox.setText("");
    }
}
