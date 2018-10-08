/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author nkaru_000
 */
public class LoginScreenController extends SceneController implements Initializable {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    Button login;
    @FXML
    private Hyperlink newUserLink;
    @FXML
    private Label errorMessage2;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    public void onLogin() throws SQLException {
        if (passwordField.getText().isEmpty() || usernameField.getText().isEmpty()) {
            errorMessage2.setText("Please enter all fields");
        } else {
            try{
                DBM dbm = DBM.sharedDBM();
                char c = usernameField.getText().charAt(0);
                if (c == 'c' || c == 'C'){
                    ResultSet verifyC = dbm.verifyCustomer(usernameField.getText(), passwordField.getText());
                    if (verifyC != null) {
                        String username = verifyC.getString("Username");
                        fancyHotelsControl.goToCustomerFunctionalityScreen(username.toString());
                    } else {
                        errorMessage2.setText("Username/Password combination not found");
                    }
                } else if (c == 'm' || c == 'M') {
                    ResultSet verifyM = dbm.verifyManager(usernameField.getText(), passwordField.getText());
                    if (verifyM != null) {
                        String username = verifyM.getString("Username");
                        fancyHotelsControl.goToManagerFunctionalityScreen(username);
                    } else {
                        errorMessage2.setText("Username/Password combination not found");
                    }
                    
                } else {
                    errorMessage2.setText("Username/Password combination not found");
                }
            } catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    @FXML 
    public void onNewUser() {
        fancyHotelsControl.goToNewUserScreen();
    }
}
