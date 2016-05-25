/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package newserver;
package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class NewThread extends Thread {
     ServerSocket sSocket;
     Socket cSocket;
     HashMap<Long, String> ID_Table;
    
    public NewThread(ServerSocket sSocket, HashMap<Long, String> pID_Table) {
        this.sSocket = sSocket;
        ID_Table = pID_Table;
        
    }

    @Override
    public void run() {

        while (true) {

            try {
                
                while (true) {
                    cSocket = sSocket.accept();
                    //String path = System.getProperty("user.dir");
                    new NormalCommands(cSocket,ID_Table).start();

                }

            } catch (IOException e) {
                //System.out.print("");
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

}
