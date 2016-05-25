
package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

class PutThread extends Thread {

    String wholecommand;
    Socket cs;

    public PutThread(String wholecommand, Socket cs) {
        this.wholecommand = wholecommand;
        this.cs = cs;
    }

    @Override
    public void run() {
        try {
            //System.out.println("Got &");
            String reply = "";
            DataInputStream in = new DataInputStream(cs.getInputStream());
            DataOutputStream out = new DataOutputStream(cs.getOutputStream());
            String filename = wholecommand.substring(4, wholecommand.length());
            try {
                int ch;
                StringBuilder strContent = new StringBuilder("");
                File file = new File(filename);

                if (file.exists() == true) {
                    FileInputStream fis = new FileInputStream(file);
                    while ((ch = fis.read()) != -1) {
                        strContent.append((char) ch);
                    }
                    String str = strContent.toString();
                    out.writeUTF(str);
                    reply = in.readUTF();
                    //System.out.println(reply);
                    fis.close();
                } else {
                    out.writeUTF("nofile");
                    reply = in.readUTF();
                    //System.out.println(reply);
                }
            } 
            catch (FileNotFoundException e) {
                System.out.println("File could not be found on filesystem");
            } catch (IOException ioe) {
                System.out.println("Exception while reading the file" + ioe);
            }

        } catch (Exception ex) {
            System.out.println("Error");
        }
    }

}
