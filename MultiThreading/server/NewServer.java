/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package newserver;
package server;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.HashMap;

public class NewServer {

    public static HashMap<Long, String> ID_Table = new HashMap<>();
   
    public static void main(String args[]) {
        
        ServerSocket sSocket = null;
        ServerSocket sSocket1 = null;
        
        //int connectionNum = 0;
        
        System.out.println("Enter Normal Port number: ");
        Scanner scan = new Scanner(System.in);
        int nPort = scan.nextInt();
        System.out.println("N_port number entered is " + nPort);
        
        System.out.println("Enter Terminate Port number: ");
        Scanner scan1 = new Scanner(System.in);
        int tPort = scan1.nextInt();
        System.out.println("T_port number entered is " + tPort);
        
        try {
            sSocket = new ServerSocket(nPort);
            sSocket1 = new ServerSocket(tPort);
            }catch (IOException ex) {
            System.out.println(ex);
        }
        
         new NewThread(sSocket,ID_Table).start();
         new TerminatePort(sSocket1,ID_Table).start();
         
                
    }
}
