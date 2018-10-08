/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class ReservationReportScreenController extends SceneController implements Initializable {

    @FXML
    private TableColumn monthCol;
    @FXML
    private TableColumn locationCol;
    @FXML
    private TableColumn numReservationsCol;
    @FXML
    private TableView reservationTable;
    private String username;
    @FXML
    private Button back;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthCol.setCellValueFactory(new PropertyValueFactory("month"));
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        numReservationsCol.setCellValueFactory(new PropertyValueFactory("numReservations"));
        //System.out.println("report inits");
        try {
            DBM dbm = DBM.sharedDBM();
            ResultSet results = dbm.getAugReservationReport();
            List<String> list1 = new ArrayList<>();
            List<String> list2 = new ArrayList<>();
            while(results.next()) {
                list1.add(results.getString("Location"));
                list2.add(results.getString(results.getMetaData().getColumnName(2)));
            }
            ResultSet resultsSept = dbm.getSeptReservationReport();
            List<String> list3 = new ArrayList<>();
            List<String> list4 = new ArrayList<>();
            while(resultsSept.next()) {
                list3.add(resultsSept.getString("Location"));
                list4.add(resultsSept.getString(resultsSept.getMetaData().getColumnName(2)));
            }
            ObservableList<Reservation> reservations = FXCollections.observableArrayList();
            for (int j = 0; j < list1.size(); j++) {
                Reservation newReservation = new Reservation("August", list1.get(j), list2.get(j));
                reservations.add(newReservation);
            }
            for (int j = 0; j < list3.size(); j++) {
                Reservation newReservation = new Reservation("September", list3.get(j), list4.get(j));
                reservations.add(newReservation);
            }
            reservationTable.setItems(reservations);
        } catch (SQLException e) {
            System.out.println("SQLException");
        }
    }    
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void onBack() {
        fancyHotelsControl.goToManagerFunctionalityScreen(username);
    }
}
