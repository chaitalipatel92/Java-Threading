
//package client;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    
     public static void main(String args[]){
         try{
     
                System.out.println("Enter machine name ");
		Scanner scan1= new Scanner(System.in);
		String f3= scan1.nextLine();
                String st="";
		
		System.out.println("enter port number:");
                Scanner scan2= new Scanner(System.in);
                                BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
                                int num2= scan2.nextInt();
                Socket cs = new Socket(f3,num2);
                DataInputStream in=new DataInputStream(cs.getInputStream()); 
		DataOutputStream out=new DataOutputStream(cs.getOutputStream());
             //   BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader bin= new BufferedReader(new InputStreamReader(cs.getInputStream()));
                String reply="";
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
                
                wholecommand=kb.readLine();
                Scanner scan = new Scanner(System.in);
                System.out.println("\n");
                
                
                if(wholecommand.length()==2 && wholecommand.substring(0,2).compareTo("ls")==0)
                {   
                    out.writeUTF(wholecommand);
                    reply = in.readUTF();
                    System.out.println(reply);

                }
                
                else if(wholecommand.substring(0,2).compareTo("cd")==0)
                {
                 out.writeUTF(wholecommand);
                 reply = in.readUTF();
                 System.out.println(reply);
                }
                
                else if(wholecommand.substring(0, 3).compareTo("get")==0)
                 {
                        out.writeUTF(wholecommand);
                        String filename= wholecommand.substring(4,wholecommand.length());
                        reply = in.readUTF();
                        File file = new File(filename);
                   
                   if(reply.compareTo("nofile")==0)
                        {
                        System.out.println("File does not exists at "+ file.getAbsolutePath());
                        }
                   else
                        {
                        PrintWriter writer = new PrintWriter(file);
                        writer.print(reply);
                        writer.close();
                        System.out.println("File copied to local directory !");
                        }
                   }
                
                else if(wholecommand.substring(0, 3).compareTo("put")==0)
                 {
                   out.writeUTF(wholecommand);
                   String filename= wholecommand.substring(4,wholecommand.length());
                   File file = new File(filename);
                   FileInputStream fis = null;
                   int ch;
                   StringBuffer strContent = new StringBuffer("");
   
                    try
                    {
                       fis = new FileInputStream(file);
     
                       while( (ch = fis.read()) != -1)
                       strContent.append((char)ch);
                       
                       String str = strContent.toString();
                       out.writeUTF(str);
                    
		   System.out.println("File copied to remote directory !");
                            fis.close();
                    }
                    catch(FileNotFoundException e)
                    {
                        System.out.println("File " + file.getAbsolutePath() + " could not be found on filesystem");
                    }
                    catch(IOException ioe)
                    {
                        System.out.println("Exception while reading the file" + ioe);
                    }
                     
                    
                 }
                
                else if(wholecommand.length()==3 && wholecommand.compareTo("pwd") == 0)
                  {
                    out.writeUTF(wholecommand);
                    reply = in.readUTF();
                    System.out.println("Current working directory on remote machine is : \n" +reply);
                  }
               
                else if(wholecommand.compareTo("cd ..")==0)
                {
                    out.writeUTF(wholecommand);
                    reply = in.readUTF();
                    System.out.println(reply);
                }
                
                else if(wholecommand.compareTo("quit") == 0)
                {
                    //out.writeUTF(wholecommand);
                    System.out.println("Connection closed");
                    cs.close();
                    System.exit(0);
                    
                }
                
                else if(wholecommand.substring(0,5).compareTo("mkdir")==0)
                  {
                    out.writeUTF(wholecommand);
                    reply = in.readUTF();
                    System.out.println(reply);
                }
               
                else if(wholecommand.substring(0, 6).compareTo("delete")==0)
                  {
                   out.writeUTF(wholecommand);
                   reply = in.readUTF();
                   System.out.println(reply);
                  }
                 
                
                  }while(st.equals("YES"));
                
                
     }
     catch(IOException e)
     {
         System.out.println("IO : " + e.getMessage());
     }
     catch(Exception ex) 
     {
         System.out.println("Invalid command");
     }
       
  }
     
}
