/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fancyhotels;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Dana
 */
public class CustomerFunctionalityScreenController extends SceneController implements Initializable {

    @FXML
    private Button newReservationLink;
    @FXML
    private Button updateReservationLink;
    @FXML
    private Button cancelReservationLink;
    @FXML
    private Button giveFeedbackLink;
    @FXML
    private Button viewFeedbackLink;
    
    private String username;
    
    @FXML
    private Label welcome;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //System.out.println(username);
    }
    
    @FXML
    public void onNewReservation() {
        fancyHotelsControl.goToSearchRoomsScreen(username);
    }
    
    @FXML
    public void onUpdateReservation() {
        fancyHotelsControl.goToUpdateReservationScreen(username);
    }
    
    @FXML
    public void onCancelReservation() {
        fancyHotelsControl.goToCancelReservationScreen(username);
    }
    
    @FXML
    public void onGiveFeedback() {
        fancyHotelsControl.goToGiveReviewScreen(username);
    }

    @FXML
    private void onViewFeedback() {
        fancyHotelsControl.goToViewReviewScreen(username);
    }
    
    public void setUsername(String username) {
        this.username = username;
        welcome.setText("Welcome, " + username);
    }   
    
    public String getUsername() {
        return username;
    }
}
