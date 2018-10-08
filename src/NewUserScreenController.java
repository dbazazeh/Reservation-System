/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fancyhotels;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
/**
 * FXML Controller class
 *
 * @author Dana
 */
public class NewUserScreenController extends SceneController implements Initializable {

    @FXML
    private PasswordField password1;
    @FXML
    private PasswordField password2;
    @FXML
    private TextField newUserName;
    @FXML
    private TextField newUserEmail;
    @FXML
    private Button submitNewUser;
    @FXML
    private Label errorMessage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        submitNewUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    DBM dbm = DBM.sharedDBM();
                    boolean check =  dbm.checkUsernameExists(newUserName.getText());

                    if (newUserName.getText().isEmpty() ||password1.getText().isEmpty() ||
                        password2.getText().isEmpty() || newUserEmail.getText().isEmpty()) {
                        errorMessage.setText("Please enter all fields");
                        errorMessage.setTextFill(Color.RED);
                    } else if (newUserName.getText().charAt(0)!= 'c' && newUserName.getText().charAt(0)!= 'C' ) {
                        errorMessage.setText("Username should start with a C or c");
                        errorMessage.setTextFill(Color.RED);
                    } else if (!password1.getText().equals(password2.getText())) {
                        errorMessage.setText("Passwords do not match");
                        errorMessage.setTextFill(Color.RED); 
                    } else if (! newUserEmail.getText().contains("@") ) { 
                        errorMessage.setText("Invalid email");
                        errorMessage.setTextFill(Color.RED);
                    } else if (!check ) {
                        dbm.insertNewUser(newUserName.getText(), password1.getText(), newUserEmail.getText());
                        fancyHotelsControl.goToCustomerFunctionalityScreen(newUserName.getText());
                    } else if (check){
                        errorMessage.setText("Username already exists");
                        errorMessage.setTextFill(Color.RED);
                    }
                } catch (SQLException e){
                
                }
            }
        });
    }
}
