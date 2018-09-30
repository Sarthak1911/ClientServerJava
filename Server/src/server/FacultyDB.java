/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Sarthak Chavan
 */
public class FacultyDB {
    
    private static ArrayList<Faculty> facultyRecords;
    
    public synchronized ArrayList<Faculty> readStudents()throws FileNotFoundException, IOException, InterruptedException{
       try{ 
           
           File file = new File("faculty.dat");
           
           if(file.canRead()){
           
                DataInputStream readFrom = new DataInputStream(new FileInputStream("faculty.dat"));
                facultyRecords = new ArrayList<>();

                while(true){

                //Initialize the student object 
                Faculty faculty = new Faculty();
                //initialize the reading object
                try{
                    faculty.setId(readFrom.readInt());
                    faculty.setFirstName(readFrom.readUTF());
                    faculty.setLastName(readFrom.readUTF());
                    faculty.setEmpType(readFrom.readUTF());
                    facultyRecords.add(faculty);
                    notifyAll();
                }
                //Added exception inside while loop
                catch(EOFException eof )
                    { readFrom.close();
                        break;
                    }

                }
               
           }else{
           
               System.out.println("Wait for read on File");
               wait();
           }
           
            
        }
         catch(FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null,"File not Found");
        }
        catch(IOException exIO)
        {
           JOptionPane.showMessageDialog(null,exIO.getMessage());
        }
       
        //readFrom.close();
        
      
        return facultyRecords;
                
    }
    
    public synchronized void writeToFile(int id, String last, String first, String type) throws FileNotFoundException, IOException, InterruptedException{
    
        File file = new File("faculty.dat");
        
        if(file.canWrite()){
        
            DataOutputStream writeFacultyInfo = new DataOutputStream(new FileOutputStream(file, true));
                    
            writeFacultyInfo.writeInt(id);
            writeFacultyInfo.writeUTF(last);
            writeFacultyInfo.writeUTF(first);
            writeFacultyInfo.writeUTF(type);
            notifyAll();
            
        }else{
        
            System.out.println("Waiting to write to the file");
            wait();
            
        }
        
        
        
        
        
    }
    
}
