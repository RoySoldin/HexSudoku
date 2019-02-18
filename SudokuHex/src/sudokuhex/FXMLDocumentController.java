/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokuhex;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author Roy Soldin
 */


public class FXMLDocumentController implements Initializable {
    
    List users_data = new ArrayList<Player>();
    
    @FXML
    private TextField username_textField;
    @FXML
    private TextField password_textField;
    
    @FXML
    private TextField username_signup_textField;
    @FXML
    private TextField password_signup_textField;
    
    
    @FXML
    private Label error_label;
    
    
    @FXML
    private void handleMoveToGameScreen() throws IOException 
    {
        String username = username_textField.getText();
        String password = password_textField.getText();
        
        Player check = checkPlayer(username,password);
        
        if(check != null){
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLGameScreen2.fxml"));
            
            Stage window = (Stage)error_label.getScene().getWindow();
            
            window.setScene(new Scene(loader.load()));
            
            
            FXMLGameScreen2Controller controller = loader.<FXMLGameScreen2Controller>getController();
            controller.setPlayer(check);
            controller.setUserdata(this.users_data);
            
            

            // next line to get stage information :
      
            window.show();
        }
        else {
            error_label.setText("  Wrong username or password," + '\n' + "    sign in failed");
        }
    }
    
    @FXML
    Button exitButton;
    
    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void forgotPassword(){
        error_label.setText("Feature is not been activated yet");
    }
    
    @FXML
    private void handleSignUp(ActionEvent event){
        
        if(username_signup_textField.getText().equals("username") | username_signup_textField.getText().equals("")){
            error_label.setText("  Illegal username"+ '\n' +"    sign up failed");
            return;
        }
        if(password_signup_textField.getText().length() < 5){
            error_label.setText("  Password need to be 5 characters minimum,"+ '\n' +"    sign up failed");
            return;
        }
        if(username_signup_textField.getText().length() < 5){
            error_label.setText("  Username need to be 5 characters minimum,"+ '\n' +"    sign up failed");
            return;
        }
            
        
        Player newPlayer = new Player(username_signup_textField.getText(),password_signup_textField.getText());
        users_data.add(newPlayer);
        update_data();
        
        username_textField.setText(username_signup_textField.getText());
        password_textField.setText(password_signup_textField.getText());
        
        error_label.setText("Sign up successful, you can sign in now");
        
    }
    
    private void update_data() {
        try {
			FileOutputStream f = new FileOutputStream("login.dat");
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(users_data);

			o.close();
			f.close();
            }
        catch(Exception ex){}
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           try{
                FileInputStream fi = new FileInputStream("login.dat");
                ObjectInputStream oi = new ObjectInputStream(fi);

                // Read object
                users_data = (List) oi.readObject();
                
                oi.close();
                fi.close();
           }
           catch(Exception ex){}
           
           Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        KeyCombination esc = new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.CONTROL_ANY);
                        Runnable runEsc = ()-> closeButtonAction();
                        error_label.getScene().getAccelerators().put(esc, runEsc);
                        
                        error_label.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                            KeyCombination combi0 = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_ANY);
                            Runnable run0 = ()-> {
                                try {
                                    handleMoveToGameScreen();
                                } catch (IOException ex) {}
                            };
                            error_label.getScene().getAccelerators().put(combi0, run0);
                        });
                    }
                    });
    }

    public Player checkPlayer(String username, String password){
        for(int i=0; i<users_data.size(); i++){
            Player toCheck = (Player)users_data.get(i);
            if(toCheck.getUsername().equals(username) && toCheck.getPassword().equals(password))
                return toCheck;
        }
        return null;       
    }

    
}
