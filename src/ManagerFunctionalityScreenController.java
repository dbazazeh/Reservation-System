/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fancyhotels;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Dana
 */
public class ManagerFunctionalityScreenController extends SceneController implements Initializable {

    @FXML
    private Hyperlink viewReservationReport;
    @FXML
    private Hyperlink viewPopularRooms;
    @FXML
    private Hyperlink viewRevenueReport;
    private String username;
    @FXML
    private Label welcome;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setUsername(String username) {
        this.username = username;
        welcome.setText("Welcome, " + username);
    }
    
    @FXML
    public void onViewReservation() {
        fancyHotelsControl.goToReservationReportScreen(username);
    }
    
    @FXML
    public void onViewPopularRooms() {
        fancyHotelsControl.goToPopularRoomScreen(username);
    }
    
    @FXML
    public void onViewRevenue() {
        fancyHotelsControl.goToRevenueReportScreen(username);
    }
    
}
