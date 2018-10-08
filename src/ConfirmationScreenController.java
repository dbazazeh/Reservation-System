/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fancyhotels;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Dana
 */
public class ConfirmationScreenController extends SceneController implements Initializable {

    @FXML
    private Button backMenu;
    private String resID;
    @FXML
    private Label reservationID11;
    private String username;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        backMenu.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent e) {
        
        
        fancyHotelsControl.goToCustomerFunctionalityScreen(username);
    }
        });
        // TODO
    }    
    
    public void setResID(String resID){
        this.resID = resID;
        reservationID11.setText(resID);
    }
    public void setUsername(String username){
    
        this.username = username;
    }
    
}
