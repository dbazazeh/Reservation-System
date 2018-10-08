/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fancyhotels;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Dana
 */
public class SearchRoomsScreenController extends SceneController implements Initializable {

    @FXML
    private Button searchRooms1;
    @FXML
    private ChoiceBox LocationItem1;
    @FXML
    private DatePicker startDate1;
    @FXML
    private DatePicker endDate1;
    @FXML
    private Label errorMessage3;
    
    private String username;

    @FXML 
    private Button back;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       
        LocationItem1.getItems().add("Atlanta");
        LocationItem1.getItems().add("Charlotte");
        LocationItem1.getItems().add("Savannah");
        LocationItem1.getItems().add("Orlando");
        LocationItem1.getItems().add("Miami");
        
        searchRooms1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String location = (String)LocationItem1.getValue();
                LocalDate startDate11 = startDate1.getValue();
                LocalDate endDate11 = endDate1.getValue();
                if (startDate11 == null || endDate11 == null) {
                    errorMessage3.setText("Please enter all fields");
                } else if (startDate11.isAfter(endDate11) ) {
                    errorMessage3.setText("Invalid Dates");
                } else if (startDate11.isBefore(LocalDate.now())) {
                    errorMessage3.setText("Date should be today or later ");
                } else if (location == null){
                    errorMessage3.setText("Please enter all fields");
                } else {
                    try {
                        DBM dbm = DBM.sharedDBM();
                        Set<Integer> allRooms = dbm.findRooms(location);
                        Set<Integer> UnavailableRooms = dbm.findUnavailableRooms(location, startDate11, endDate11);
                        allRooms.removeAll(UnavailableRooms);
                        //System.out.println(allRooms);
                        ResultSet reservationTable = dbm.getReservationTable(allRooms);
                        fancyHotelsControl.goToReservationScreen(username, reservationTable, startDate11, endDate11, location);
                    } catch (SQLException e){
                        
                    }
                }
            }
        });
        
    }    
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @FXML
    public void onBack() {
        fancyHotelsControl.goToCustomerFunctionalityScreen(username);
    }
}
