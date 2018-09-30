/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sarthak Chavan
 */
public class Server implements Runnable {

    /**
     * @param args the command line arguments
     */
    
    Socket socket;
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        //Create a Server Socket
        ServerSocket serverConnect = new ServerSocket(8000);
        
        while(true){
        
            //Activate the Socket
            Socket socket = serverConnect.accept();

            new Thread(new Server(socket)).start();
            
        }
        
        
            
        
    }

    @Override
    public void run() {
        
        Faculty selectedFaculty = null;
        
        try{
        
            //Listen for incoming messages form the client
            DataInputStream fromClient = new DataInputStream(this.socket.getInputStream());
            //Send messages to the Client
            DataOutputStream toClient = new DataOutputStream(this.socket.getOutputStream());
            //Create object of FacultyDB
            FacultyDB fDB = new FacultyDB();
            //Server is now ON
            System.out.println("CLIENT CONNECTED");
            
            //Get the server in perpetual active mode
            while(true){
            
                String flag = fromClient.readUTF();
                //System.out.println(flag);
                
                //Listen to int
                if(flag.equalsIgnoreCase("POST")){
                
                    System.out.println("REQUEST IS POST");
                    
                    fDB.writeToFile(fromClient.readInt(), fromClient.readUTF(), fromClient.readUTF(), fromClient.readUTF());
                    
//                    DataOutputStream writeFacultyInfo = new DataOutputStream(new FileOutputStream("faculty.dat", true));
//                    
//                    writeFacultyInfo.writeInt(fromClient.readInt());
//                    writeFacultyInfo.writeUTF(fromClient.readUTF());
//                    writeFacultyInfo.writeUTF(fromClient.readUTF());
//                    writeFacultyInfo.writeUTF(fromClient.readUTF());
                    
                    System.out.println("Faculty Saved!");
                    
                }
                
                if(flag.equalsIgnoreCase("GET")){
                
                    System.out.println("REQUEST IS GET");
                    
                    int id = fromClient.readInt();
                    
                    ArrayList<Faculty> list = fDB.readStudents();
                    
                    for (int i = 0; i < list.size(); i++) {
                        
                        if(list.get(i).getId() == id){
                        
                            selectedFaculty = list.get(i);
                            break;
                            
                        }
                        
                    }
                    
                    if(selectedFaculty != null){
                    
                        toClient.writeUTF("Y");
                        toClient.writeInt(selectedFaculty.getId());
                        toClient.writeUTF(selectedFaculty.getFirstName());
                        toClient.writeUTF(selectedFaculty.getLastName());
                        toClient.writeUTF(selectedFaculty.getEmpType());
                        
                    }else{
                    
                        toClient.writeUTF("N");
                        System.out.println("Record Not Found");
                        
                    }
                    
                    
                    
                }
                
                
            }
            
        }catch(IOException io){
        
            System.out.println(io.getMessage());
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public Server(Socket socket) {
        this.socket = socket;
    }
    
}
