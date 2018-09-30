/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Sarthak Chavan
 */
public class Client extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        Scene scene = new Scene(getRoot(), 500, 500);

        primaryStage.setTitle("Client Side");
        primaryStage.setScene(scene);
        primaryStage.show();
   
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public BorderPane getRoot(){

            //TextField  
            TextField id = new TextField();
            id.setPadding(new Insets(5, 5, 5, 5));
            TextField last = new TextField();
            last.setPadding(new Insets(5, 5, 5, 5));
            TextField first = new TextField();
            first.setPadding(new Insets(5, 5, 5, 5));


            //Labels
            Label lb1 = new Label("ID:");        
            Label lb4 = new Label("Last Name:");
            Label lb5 = new Label("First Name:");
            Label lb2 = new Label("Type:");

            //HBOx for functionality button
            HBox paneButton = new HBox(20); 
            Button btnPost = new Button("POST");
            Button btnGet = new Button("GET");
            paneButton.getChildren().addAll(btnPost, btnGet);
            paneButton.setAlignment(Pos.CENTER);
            paneButton.setPadding(new Insets(20, 20, 20, 20));

            //VBox for TextArea
            VBox paneTextArea = new VBox(40);
            TextArea displayArea = new TextArea();
            paneTextArea.getChildren().addAll(displayArea);
            paneTextArea.setAlignment(Pos.CENTER);
            paneTextArea.setPadding(new Insets(5, 10, 5, 10));

            HBox paneRadio = new HBox(40);
            RadioButton full_rad = new RadioButton();
            RadioButton part_rad = new RadioButton();
            ToggleGroup toggleGroup = new ToggleGroup();
            full_rad.setText("Full Time");
            part_rad.setText("Part Time");
            full_rad.setToggleGroup(toggleGroup);
            part_rad.setToggleGroup(toggleGroup);
            full_rad.setUserData("Full Time");
            part_rad.setUserData("Part Time");
            paneRadio.getChildren().addAll(full_rad, part_rad);
            paneRadio.setPadding(new Insets(5, 5, 5, 5));

            //GridPane
            GridPane gridPane = new GridPane();
            gridPane.setVgap(5);
            gridPane.setHgap(5);
            gridPane.setAlignment(Pos.CENTER);
            gridPane.add(lb1, 0, 0);
            gridPane.add(id, 1, 0);
            gridPane.add(lb4, 0, 2);
            gridPane.add(first, 1, 2);
            gridPane.add(lb5, 0, 3);
            gridPane.add(last, 1, 3);
            gridPane.add(lb2, 0, 4);
            gridPane.add(paneRadio, 1, 4);
            gridPane.setPadding(new Insets(20, 20, 20, 20));

            //BorderPane
            BorderPane bPane = new BorderPane();
            bPane.setLeft(gridPane);
            bPane.setCenter(paneTextArea);
            bPane.setBottom(paneButton);
            
            try{
            
                //Get on the same socket as the server
                Socket socket = new Socket("localhost", 8000);
                //Send messages to the server
                DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
                //Get messages from the server
                DataInputStream fromServer = new DataInputStream(socket.getInputStream());
                    
                btnPost.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                        try {
                            
                            if ("".equals(id.getText()) || id.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Enter ID");
                            }
                            else if ("".equals(first.getText()) || first.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please Enter First Name");
                                }

                            else if ("".equals(last.getText()) || last.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please Enter Last Name");
                                }
                            else
                            {
                                toServer.writeUTF("POST");
                                toServer.writeInt(Integer.parseInt(id.getText()));
                                toServer.writeUTF(first.getText());
                                toServer.writeUTF(last.getText());
                                toServer.writeUTF(toggleGroup.getSelectedToggle().getUserData().toString());
                                
                                id.setText("");
                                first.setText("");
                                last.setText("");
                                full_rad.setSelected(false);
                                part_rad.setSelected(false);
                                displayArea.setText("");
                                JOptionPane.showMessageDialog(null,"Faculty Saved!");
                            }
                            
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }catch(Exception e){
                        
                            System.out.println(e.getMessage());
                            
                        }

                    }
                });

                btnGet.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                    
                        String faculty = "";
                        
                        try {
                            
                            if ("".equals(id.getText()) || id.getText().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Enter ID");
                            }else{
                            
                                toServer.writeUTF("GET");
                                toServer.writeInt(Integer.parseInt(id.getText()));
                                //Get data from server
                                
                                String flag = fromServer.readUTF();
                                
                                if(flag.equalsIgnoreCase("Y")){
                                
                                    faculty =   "ID: " + fromServer.readInt() + "\n" +
                                            "First Name: " + fromServer.readUTF()+ "\n" +
                                            "Last Name: " + fromServer.readUTF() + "\n" +
                                            "Employment Type: " + fromServer.readUTF() + "\n";

                                    displayArea.setText(faculty);
                                    id.setText("");
                                    
                                }
                                
                                if(flag.equalsIgnoreCase("N")){
                                
                                    JOptionPane.showMessageDialog(null, "No Data Found");
                                    id.setText("");
                                    
                                }
                                
                                
                                
                            }
                            
                            
                            
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                        
                        
                    }
                });
                
            }catch(IOException io){
            
                System.out.println(io.getMessage());
                
            }

            return bPane;

        }
    
}
