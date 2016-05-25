/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package newserver;
package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TerminatePort extends Thread {

    ServerSocket sSocket;
    Socket cSocket1;
    HashMap<Long, String> ID_Table;
    public TerminatePort(ServerSocket sSocket,HashMap<Long, String> pID_Table) {
        this.sSocket = sSocket;
        ID_Table = pID_Table;
    }

    
    
    @Override
    public void run() {
        while (true) {
            try {
                cSocket1 = sSocket.accept();
                System.out.println("I am calling!!");
                DataOutputStream dataout = new DataOutputStream(cSocket1.getOutputStream());
                DataInputStream datain = new DataInputStream(cSocket1.getInputStream());
                String wholecommand = datain.readUTF();
                
                new TerminateCommand(dataout,datain,wholecommand,ID_Table).start();
               
            } catch (IOException ex) {
                System.out.println("");
            }
        }
    }
    
    public static class TerminateCommand{
    boolean run = true;
    DataOutputStream dataout;
    DataInputStream datain;
    String wholecommand;
    HashMap<Long, String> ID_Table ;
   
    public TerminateCommand(DataOutputStream dataout, DataInputStream datain, String wholecommand,HashMap<Long, String> pID_Table ) {
        this.dataout = dataout;
        this.datain = datain;
        this.wholecommand = wholecommand;
        ID_Table = pID_Table;
    }  
    
    public void start() {
       while (run) {
            try {
                if (wholecommand.substring(0, 9).compareTo("terminate") == 0)
                {
                    System.out.println("Command Received : " + wholecommand);
                    String CommandId = wholecommand.substring(10, wholecommand.length());
                    //System.out.println(CommandId);
                    Long ID = Long.parseLong(CommandId);
                    //System.out.println(ID);
                    if(ID_Table.containsKey(ID) && ID_Table.get(ID)=="isAlive")
                    {
                         ID_Table.remove(ID);
                         ID_Table.put(ID, "terminate");
                         System.out.println("Terminated");
                         run=false;
                    }
                   
                    
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
       }
    }
}
}
