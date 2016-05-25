/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import java.io.DataInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;

class GetThread extends Thread {

    String wholecommand;
    Socket cs;

    public GetThread(String wholecommand, Socket cs) {
        this.wholecommand = wholecommand;
        this.cs = cs;
    }

    @Override
    public void run() {
        try {
            String reply = "";
            DataInputStream in = new DataInputStream(cs.getInputStream());
            reply = in.readUTF();
            String filename = wholecommand.substring(4, wholecommand.length());
            File file = new File(filename);

            if (reply.compareTo("nofile") == 0) {
                System.out.println("File does not exists");
            } else {
                PrintWriter writer = new PrintWriter(file);
                writer.print(reply);
                writer.close();
                //System.out.println("File copied to local directory !");
                this.interrupt();
            }
        } catch (Exception ex) {
            System.out.println("Error");
        }
    }

}
