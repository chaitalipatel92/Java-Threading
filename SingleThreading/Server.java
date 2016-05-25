



//package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String args[]) {
            System.out.println("enter port number: ");
            Scanner scan = new Scanner(System.in);
            int num = scan.nextInt();
            System.out.println("the port number entered is " + num);
            while(true)
            {
               ServerSocket sSocket;
               Socket cSocket;   
            try {
            sSocket = new ServerSocket(num);        
            System.out.println("Waiting for Client...");
            
            
            cSocket = sSocket.accept();            
            System.out.println("Connection Eshtablished.");
            
            while (true) {
            
                DataOutputStream dataout = new DataOutputStream(cSocket.getOutputStream());    
                DataInputStream datain = new DataInputStream(cSocket.getInputStream());       
            
            
                System.out.println("Waiting for Command....");
                String wholecommand = datain.readUTF();
                if (wholecommand.compareTo("ls")==0) 
                    {
                        System.out.println("Command Received : " + wholecommand);
                        String directory = System.getProperty("user.dir");
                        File f1 = new File(directory);
                        StringBuffer sContent = new StringBuffer("");
                    
                        if (f1.isDirectory()) 
                            {
                            String s[] = f1.list();
                            for (int i = 0; i < s.length; i++) 
                                {
                                sContent.append(s[i]+"\n");
                                }
                            String str = sContent.toString();
                            dataout.writeUTF(str);
                            } 
                        else 
                        {
                          System.out.println("Not a directory");
                        }
                    }
                
                    //GET <filename> Command
                    else if (wholecommand.substring(0, 3).compareTo("get") == 0) 
                    {
                    System.out.println("Command Received : " + wholecommand);
                    String filename = wholecommand.substring(4, wholecommand.length());
                     
                    File file1 = new File(filename);
                   
                    int ch;
                    StringBuffer stContent = new StringBuffer("");
                   //System.out.println(fis1. + " absbd");
                    
                       
                    if((file1.exists()== false))
                       {
                           String temp = "nofile";
                          dataout.writeUTF(temp);
                           
                       }
                        
                       else
                       {
                         FileInputStream fis1 =  new FileInputStream(file1);
                           while( (ch = fis1.read()) != -1)
                           {
                                stContent.append((char)ch);
                           }
                        fis1.close();
                        String sendContent = stContent.toString();
                        dataout.writeUTF(sendContent);
                        }
                       
                    
                    } 
                
                else if (wholecommand.substring(0, 3).compareTo("put") == 0) 
                   {
                   System.out.println("Command Received : " + wholecommand);
                   String filename= wholecommand.substring(4,wholecommand.length());
                   String reply = datain.readUTF();
                   File file = new File(filename);
                   PrintWriter fWriter = new PrintWriter(file);
                   fWriter.print(reply);
                   
                   fWriter.close();
                   } 
                
                else if (wholecommand.compareTo("pwd") == 0) 
                {
                    System.out.println("Command Received : " + wholecommand);
                    String abc = System.getProperty("user.dir");
                    dataout.writeUTF(abc);
	 	            
                } 
                
                else if(wholecommand.compareTo("cd ..")==0) 
	            {
                                System.out.println("Command Received : " + wholecommand);
                                File changedir = new File(System.getProperty("user.dir")).getParentFile();
		        	//System.out.println("" +changedir);
		        	String temp=String.valueOf(changedir);
		        	System.setProperty("user.dir",temp);
		        	dataout.writeUTF(System.getProperty("user.dir"));
	            }
	            
                else if(wholecommand.substring(0,2).compareTo("cd")==0) 
	            {
                        String temp2=wholecommand.substring(3,wholecommand.length());
                	File changedir1 = new File(System.getProperty("user.dir") + "/"+temp2);
                        File changedir2 = new File(System.getProperty("user.dir"));
                        if(!changedir1.exists())
                        {
                            dataout.writeUTF("Directory does not exist");
                           
                        }
                        else
                        {
                	System.setProperty("user.dir",changedir2.getAbsolutePath()+"/"+temp2);
                	dataout.writeUTF(System.getProperty("user.dir"));
		    }
                    }
                
                else if(wholecommand.substring(0,5).compareTo("mkdir")==0)
	            {
                        System.out.println("Command Received : " + wholecommand);
	            	String cwd=System.getProperty("user.dir");
	            	String filename= wholecommand.substring(6,wholecommand.length());
	            	File file = new File(cwd+"//"+filename);
	            	if (!file.exists())
	            	{
                            if (file.mkdir())
                                dataout.writeUTF("Directory is created!");
	        	}
	        	    else
	        		dataout.writeUTF("Failed to create directory!");
                    }
                
                else if (wholecommand.substring(0, 6).compareTo("delete") == 0) 
                    {
                        System.out.println("Command Received : " + wholecommand);
                        String filename = wholecommand.substring(7, wholecommand.length());
                        File file = new File(filename);
                        if (file.exists()) 
                        {
                            file.delete();
                            dataout.writeUTF("File deleted");
                        } 
                        else 
                        {
                            dataout.writeUTF("File not found");
                        }
                    }
                
            }
                  
        } catch (IOException e) {
            System.out.print("");
                
        } catch(Exception ex) {
             System.out.println(ex);
        }
          
    }
    }
}

