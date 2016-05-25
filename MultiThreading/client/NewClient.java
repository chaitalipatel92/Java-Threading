/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import java.io.*;
import java.net.*;
import java.util.*;

public class NewClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            System.out.println("Enter machine name ");
            Scanner scan1 = new Scanner(System.in);
            String machineName = scan1.nextLine();
            System.out.println("Enter Normal Port number: ");
            Scanner scan2 = new Scanner(System.in);
            int nPort = scan2.nextInt();
            System.out.println("Enter Terminate Port number: ");
            Scanner scan3 = new Scanner(System.in);
            int tPort = scan3.nextInt();
            
            Socket cs = new Socket(machineName, nPort);
            Socket cs2 = new Socket(machineName, tPort);
            DataInputStream in = new DataInputStream(cs.getInputStream());
            DataInputStream in2 = new DataInputStream(cs.getInputStream());
            DataOutputStream out = new DataOutputStream(cs.getOutputStream());
            //DataOutputStream out2 = new DataOutputStream(cs.getOutputStream());
            DataInputStream din = new DataInputStream(cs2.getInputStream());
            DataOutputStream dout = new DataOutputStream(cs2.getOutputStream());
            BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
            String reply = "", reply2="";
            String wholecommand;

            do {

                System.out.println("\nEnter the command you wish to execute: ");
                System.out.println("-> get <fileName>");
                System.out.println("-> put <fileName>");
                System.out.println("-> delete <fileName>");
                System.out.println("-> ls");
                System.out.println("-> cd");
                System.out.println("-> mkdir <directoryName>");
                System.out.println("-> pwd");
                System.out.println("-> quit\n");
                
                System.out.print("myftp>");

                wholecommand = kb.readLine();
                System.out.println("\n");

                if (wholecommand.length() == 2 && wholecommand.substring(0, 2).compareTo("ls") == 0) {
                    out.writeUTF(wholecommand);
                    reply = in.readUTF();
                    System.out.println(reply);
                } 
                
                
                else if (wholecommand.substring(0, 2).compareTo("cd") == 0) {
                    out.writeUTF(wholecommand);
                    reply = in.readUTF();
                    System.out.println(reply);
                } 
                
                
                else if (wholecommand.substring(0, 3).compareTo("get") == 0) {
                    
                    if(wholecommand.substring(wholecommand.length()-1).compareTo("&")==0)
                    {
                        wholecommand = wholecommand.substring(0, (wholecommand.length() - 1));
                        out.writeUTF(wholecommand);
                        reply2 = in2.readUTF();
                        System.out.println("Your Command-Id is " + reply2);
                        new GetThread(wholecommand,cs).start();
                    }
                    else
                    {
                    if(wholecommand.length()<5)
                    {
                        System.out.println("Enter the file name");
                    }
                    else
                    {
                    out.writeUTF(wholecommand);
                    reply2 = in2.readUTF();
                    System.out.println("Your Command-Id is " + reply2);
                    
                    reply = in.readUTF();
                    String filename = wholecommand.substring(4, wholecommand.length());
                    File file = new File(filename);

                    if (reply.compareTo("nofile") == 0) {
                        System.out.println("File does not exists");
                    } else {
                        
                        PrintWriter writer = new PrintWriter(file);
                        writer.print(reply);
                        writer.close();
                        System.out.println("File copied to local directory !");
                        }
                    }
                    }
                } 
                
                
                else if (wholecommand.substring(0, 3).compareTo("put") == 0) 
                   {
                    
                    if(wholecommand.substring(wholecommand.length()-1).compareTo("&")==0)
                    {
                        wholecommand = wholecommand.substring(0, (wholecommand.length() - 1));
                        out.writeUTF(wholecommand);
                        reply2 = in2.readUTF();
                        System.out.println("Your Command-Id is " + reply2);
                        new PutThread(wholecommand,cs).start();
                    }
                    else
                    {
                    
                    if(wholecommand.length()<5)
                    {
                        System.out.println("Enter the file name");
                    }
                    else
                    {
                   out.writeUTF(wholecommand);
                   String filename= wholecommand.substring(4,wholecommand.length());
                   reply2=in2.readUTF();
                   System.out.println("Your command id is " + reply2);
                    try
                    {
                        int ch;
                        StringBuilder strContent = new StringBuilder("");
                        File file = new File(filename);
                        
                        if(file.exists()==true)
                        {
                        FileInputStream fis = new FileInputStream(file);
                        while( (ch = fis.read()) != -1)
                       {
                        strContent.append((char)ch);
                       }
                       String str = strContent.toString();
                       out.writeUTF(str);
                       reply = in.readUTF();
                       System.out.println(reply);
                        fis.close();
                        }
                        else
                        {
                            out.writeUTF("nofile");
                            reply = in.readUTF();
                            System.out.println(reply);
                        }
                    }
                    catch(FileNotFoundException e)
                    {
                        System.out.println("File could not be found on filesystem");
                    }
                    catch(IOException ioe)
                    {
                        System.out.println("Exception while reading the file" + ioe);
                    }
                 }
                } 
                }
                 else if (wholecommand.length() == 3 && wholecommand.compareTo("pwd") == 0) {
                    out.writeUTF(wholecommand);
                    reply = in.readUTF();
                    System.out.println("Current working directory on remote machine is : \n" + reply);
                } 
                
                
//                else if (wholecommand.compareTo("cd ..") == 0) {
//                    out.writeUTF(wholecommand);
//                    reply = in.readUTF();
//                    System.out.println(reply);
//                } 
                
                
                else if (wholecommand.compareTo("quit") == 0) {
                    System.out.println("Connection closed");
                     out.writeUTF(wholecommand);
                    cs.close();
                    System.exit(0);

                } 
                
                
                else if (wholecommand.substring(0, 5).compareTo("mkdir") == 0) {
                    out.writeUTF(wholecommand);
                    reply = in.readUTF();
                    System.out.println(reply);
                } 
                
                
                else if (wholecommand.substring(0, 6).compareTo("delete") == 0) {
                    out.writeUTF(wholecommand);
                    reply = in.readUTF();
                    System.out.println(reply);
                }
                
                else if (wholecommand.substring(0, 9).compareTo("terminate") == 0) {
                    dout.writeUTF(wholecommand);
                    //reply = din.readUTF();
                    //System.out.println("Terminated send" + reply);
                } 

                //System.out.println("\n Do you wish to continue? (yes/no) \n");
                //st = kb.readLine();
                //st = st.toUpperCase();
            }while(true); 
            //while (st.equals("YES"));

        } catch (IOException e) {
            System.out.println("IO : " + e.getMessage());
        } catch (Exception ex) {
            System.out.println("Invalid command");
        }

    }
    
}
