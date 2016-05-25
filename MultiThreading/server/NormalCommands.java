/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class NormalCommands extends Thread {

    HashMap map = new HashMap();
    Socket cSocket;
    boolean running = true;
    HashMap<Long, String> ID_Table;
    boolean flag = false;
    private Path path;
    private List<String> tokens;
    //String abc = "";

    public NormalCommands(Socket cSocket, HashMap<Long, String> pID_Table) {
        this.cSocket = cSocket;
        ID_Table = pID_Table;
        path = Paths.get(System.getProperty("user.dir"));
    }

    @Override
    public void run() {
        while (running) {
            try {
                //System.out.println("I am calling to terminate:" + this.getId());
                DataOutputStream dataout = new DataOutputStream(cSocket.getOutputStream());
                DataInputStream datain = new DataInputStream(cSocket.getInputStream());
                String wholecommand = datain.readUTF();
                tokens = new ArrayList<String>();
                Scanner tokenize = new Scanner(wholecommand);
				//gets command
				if (tokenize.hasNext())
				    tokens.add(tokenize.next());
				//gets rest of string after the command; this allows filenames with spaces: 'file1 test.txt'
				if (tokenize.hasNext())
					tokens.add(wholecommand.substring(tokens.get(0).length()).trim());
				tokenize.close();

                //ls Command
                if (wholecommand.compareTo("ls") == 0) 
                {
                    try {
			DirectoryStream<Path> dirStream = Files.newDirectoryStream(path);
                        String aaa="",bbb="";
			for (Path entry: dirStream)
                        {
                                Path abc=entry.getFileName();
                                bbb = abc.toString();
                                aaa=aaa.concat("\n"+bbb);
                        }
			dataout.writeUTF(aaa);
		} catch(Exception e) {
                        System.out.println("List Error");
                    }
                }
                    
                    //get <filename> Command
                else if (wholecommand.substring(0, 3).compareTo("get") == 0) {
                        new ServerGetThread(wholecommand, cSocket,ID_Table).start();
                } 

                    //put <filename> Command
                else if (wholecommand.substring(0, 3).compareTo("put") == 0) {
                    new ServerPutThread(wholecommand, cSocket).start();
                } 

                //pwd Command
                else if (wholecommand.compareTo("pwd") == 0) {
                    System.out.println("Command Received : " + wholecommand);
                    String abc = path.toString();
                    //System.out.println(path);
                    dataout.writeUTF(abc);
                } 
                
                //cd <dest_directory> Command
                else if (wholecommand.substring(0, 2).compareTo("cd") == 0) 
                {
                    System.out.println("Inside CD");
                    try {
			//cd
			if (tokens.size() == 1) {
				path = Paths.get(System.getProperty("user.dir"));
                                String p1=path.toString();
				dataout.writeUTF(p1);
			}
			//cd ..
			if (tokens.get(1).equals("..")) {
                            //System.out.println(tokens);
				if (path.getParent() != null)
                                {
					path = path.getParent();
                                }
                                String p2=path.toString();
				dataout.writeUTF(p2);
			}
			//cd somedirectory
			else {
				//not a directory or file
				if (Files.notExists(path.resolve(tokens.get(1)))) {
					dataout.writeUTF("cd: " + tokens.get(1) + ": No such file or directory" + "\n");
				} 
				//is a directory
				else if (Files.isDirectory(path.resolve(tokens.get(1)))) {
					path = path.resolve(tokens.get(1));
                                        String p3=path.toString();
					dataout.writeUTF(p3);
				}
				//is a file
				else {
					dataout.writeUTF("cd: " + tokens.get(1) + ": Not a directory" + "\n");
				}
			}
		} catch (Exception e) {
			dataout.writeUTF("cd: " + tokens.get(1) + ": Error" + "\n");
		}
                }
                
                else if (wholecommand.compareTo("quit") == 0) {
                    running = false;
                } //mkdir <dir_name> Command
                else if (wholecommand.substring(0, 5).compareTo("mkdir") == 0) 
                {
                    try {
			Files.createDirectory(path.resolve(tokens.get(1)));
			dataout.writeUTF("Directory is created!");
		} catch(FileAlreadyExistsException falee) {
			dataout.writeUTF("mkdir: cannot create directory `" + tokens.get(1) + "': File or folder exists" + "\n");
		} catch(Exception e) {
			dataout.writeUTF("mkdir: cannot create directory `" + tokens.get(1) + "': Permission denied" + "\n");
		}
                }

                //delete <filename> Command
                else if (wholecommand.substring(0, 6).compareTo("delete") == 0) 
                {
                    try {
			boolean confirm = Files.deleteIfExists(path.resolve(tokens.get(1)));
			if (!confirm) {
				dataout.writeUTF("delete: cannot remove '" + tokens.get(1) + "': No such file" + "\n");
			} else
				dataout.writeUTF("File deleted");
		} catch(DirectoryNotEmptyException dnee) {
			dataout.writeUTF("delete: failed to remove `" + tokens.get(1) + "': Directory not empty" + "\n");
		} catch(Exception e) {
			dataout.writeUTF("delete: failed to remove `" + tokens.get(1) + "'" + "\n");
		}
                }
                
            } catch (IOException e) {
                //System.out.print("");

            } catch (Exception ex) {
                System.out.println(ex);
            }

        }

    }

    private static class ServerGetThread {

        String wholecommand;
        Socket cSocket;
        HashMap<Long, String> ID_Table;
        
        public ServerGetThread(String wholecommand, Socket cSocket,HashMap<Long, String> pID_Table) {
            this.cSocket = cSocket;
            this.wholecommand = wholecommand;
            this.ID_Table = pID_Table;
        }

        private void start() {

            try {
                long threadId = Thread.currentThread().getId();
                NewServer.ID_Table.put(threadId, "isAlive");
       
                DataOutputStream dataout = new DataOutputStream(cSocket.getOutputStream());
                DataOutputStream dataout2 = new DataOutputStream(cSocket.getOutputStream());
                String GetId = Long.toString(threadId);
                dataout2.writeUTF(GetId);

                String filename = wholecommand.substring(4, wholecommand.length());
                File file1 = new File(filename);
                int ch;
                StringBuilder stContent = new StringBuilder("");

                if ((file1.exists() == false)) {
                    String temp = "nofile";
                    dataout.writeUTF(temp);

                } else {
                    FileInputStream fis1 = new FileInputStream(file1);
                    int i = 0;
                    while ((ch = fis1.read()) != -1) {
                        i++;
                        if (i % 1000 == 0)
                        {
                            new TimelyCheck(ID_Table).start();
                        }
                        stContent.append((char) ch);
                    }
                    fis1.close();
                    String sendContent = stContent.toString();
                    dataout.writeUTF(sendContent);
                    NewServer.ID_Table.put(threadId, "terminate");
                }
            } catch (Exception ex) {
                System.out.println("Error");
            }

        }
    }

    private static class ServerPutThread {

        String wholecommand;
        Socket cSocket;

        public ServerPutThread(String wholecommand, Socket cSocket) {
            this.cSocket = cSocket;
            this.wholecommand = wholecommand;
        }

        public synchronized void start() {
            try {
                long threadId = Thread.currentThread().getId();
                NewServer.ID_Table.put(threadId, "isAlive");
                String GetId = Long.toString(threadId);
                DataOutputStream dataout2 = new DataOutputStream(cSocket.getOutputStream());
                dataout2.writeUTF(GetId);

                DataInputStream datain = new DataInputStream(cSocket.getInputStream());
                DataOutputStream dataout = new DataOutputStream(cSocket.getOutputStream());
                // System.out.println("Command Received : " + wholecommand);
                String filename = wholecommand.substring(4, wholecommand.length());
                String reply = datain.readUTF();
                if (reply.compareTo("nofile") != 0) {
                    File file = new File(filename);
                    PrintWriter fWriter = new PrintWriter(file);
                    fWriter.print(reply);
                    dataout.writeUTF("File Copied to Remote Directory!!");
                    fWriter.close();
                } else {
                    dataout.writeUTF("File Not Found");
                }
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
    }

}
