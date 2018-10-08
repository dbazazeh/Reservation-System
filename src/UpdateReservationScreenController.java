/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import java.awt.Dialog;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class UpdateReservationScreenController extends SceneController implements Initializable {

    @FXML
    TextField reservationID;
    @FXML
    Label noIDError;
    @FXML
    DatePicker curStartDate;
    @FXML
    DatePicker curEndDate;
    @FXML
    DatePicker newStartDate;
    @FXML
    DatePicker newEndDate;
    @FXML
    Label noDateError;
    @FXML
    Button searchAvailability;
    @FXML
    Label roomsAvailableMsg;
    @FXML
    Label totalCostUpdated;
    @FXML
    private Button submit;
    
    @FXML
    private TableColumn roomNum;
    @FXML
    private TableColumn roomCat;
    @FXML
    private TableColumn numPersons;
    @FXML
    private TableColumn costPerDay;
    @FXML
    private TableColumn costOfBed;
    @FXML
    private TableColumn selectExtra;
    @FXML
    private TableView updateTable;
    
    @FXML
    private Button back;
    @FXML
    private Label confirmation;
    
    private String username;
    private String location;
    private ArrayList<Integer> roomNums;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roomNums = new ArrayList();
        noIDError.setVisible(false);
        curStartDate.setDisable(true);
        curEndDate.setDisable(true);
        newStartDate.setDisable(true);
        newEndDate.setDisable(true);
        noDateError.setVisible(false);
        searchAvailability.setDisable(true);
        roomsAvailableMsg.setText("No content to display");
        totalCostUpdated.setVisible(false);
        submit.setDisable(true);
        confirmation.setVisible(false);
    }    
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @FXML
    public void onSearch() throws SQLException {
        String ID = reservationID.getText();
        if (ID.equals("") || !ID.matches("[0-9]+") || ID.length() != 5) {
            noIDError.setText("Please enter a valid reservationID");
            noIDError.setVisible(true);
        } else {
            int IDNum = Integer.parseInt(reservationID.getText());
            DBM dbm = DBM.sharedDBM();
            ResultSet reservations = dbm.getReservation(IDNum, username);
            if (!reservations.next()) {
                noIDError.setText("You have no reservations matching that reservation ID");
                noIDError.setVisible(true);                
            } else {                     
                this.location = reservations.getString("Location");
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                String strCurStart = reservations.getString("StartDate");
                LocalDate startLocalDate = LocalDate.parse(strCurStart, formatter);
                
                if(startLocalDate.isBefore(LocalDate.now())) {
                    noIDError.setText("This reservation has already passed");
                    noIDError.setVisible(true);
                    return;
                }
                
                curStartDate.setValue(startLocalDate);
                curStartDate.setDisable(false);
                
                String strCurEnd = reservations.getString("EndDate");
                LocalDate endLocalDate = LocalDate.parse(strCurEnd, formatter);
                curEndDate.setValue(endLocalDate);
                curEndDate.setDisable(false);
                
                curStartDate.setDisable(false);
                curEndDate.setDisable(false);
                newStartDate.setDisable(false);
                newEndDate.setDisable(false);
                searchAvailability.setDisable(false);
                
                reservations.beforeFirst();
                
                while(reservations.next()) {
                    this.roomNums.add(Integer.parseInt(reservations.getString("RoomNum")));
                }
            }
        }   
    }
    
    @FXML
    public void onSearchAvailability() throws SQLException {
        LocalDate csd = curStartDate.getValue();
        LocalDate nsd = newStartDate.getValue();
        LocalDate ced = curEndDate.getValue();
        LocalDate ned = newEndDate.getValue();
        LocalDate now = LocalDate.now();
        if (csd == null || nsd == null || ced == null || ned == null) {
            noDateError.setText("Please enter all dates");
            noDateError.setVisible(true);
        } else if (csd.isBefore(now) || nsd.isBefore(now) || ced.isBefore(now) || ned.isBefore(now)) {
            noDateError.setText("Please enter future dates only");
            noDateError.setVisible(true);
        } else if (ned.isBefore(nsd)) {
            noDateError.setText("Please enter an end date that is after the start date");
            noDateError.setVisible(true);
        } else {
            roomNum.setCellValueFactory(new PropertyValueFactory("roomNum"));
            roomCat.setCellValueFactory(new PropertyValueFactory("roomCat"));
            numPersons.setCellValueFactory(new PropertyValueFactory("numPersons"));
            costPerDay.setCellValueFactory(new PropertyValueFactory("costPerDay"));
            costOfBed.setCellValueFactory(new PropertyValueFactory("costOfBed"));
            selectExtra.setCellValueFactory(new PropertyValueFactory("selectExtra"));
            DBM dbm = DBM.sharedDBM();
            ArrayList<ResultSet> updates = dbm.displayUpdateOptions(Integer.parseInt(reservationID.getText()), nsd, ned, this.location, this.roomNums);
            //FIX THIS UPDATES STUFF
            if (updates != null) {            
                roomsAvailableMsg.setText("Rooms are available. Please confirm details below before submitting.");
                submit.setDisable(false);
                ObservableList<Update> rows = FXCollections.observableArrayList();
                for (int i = 0; i < updates.size(); i++) {
                    ResultSet rs = updates.get(i);
                    List<String> list1 = new ArrayList<>();
                    List<String> list2 = new ArrayList<>();
                    List<String> list3 = new ArrayList<>();
                    List<String> list4 = new ArrayList<>();
                    List<String> list5 = new ArrayList<>();
                    List<String> list6 = new ArrayList<>();
                    
                    while(rs.next()) {
                        list1.add(rs.getString(rs.getMetaData().getColumnName(1)));
                        list2.add(rs.getString(rs.getMetaData().getColumnName(2)));
                        list3.add(rs.getString(rs.getMetaData().getColumnName(3)));
                        list4.add(rs.getString(rs.getMetaData().getColumnName(4)));
                        list5.add(rs.getString(rs.getMetaData().getColumnName(5)));
                        list6.add(rs.getString(rs.getMetaData().getColumnName(6)));
                    }
                    
                    for (int j = 0; j < list1.size(); j++) {
                        Update newUpdate = new Update(list1.get(j), list2.get(j), list3.get(j), list4.get(j), list5.get(j), list6.get(j));
                        rows.add(newUpdate);
                    }
                }
                //System.out.println(rows.get(0).getRoomNum());
                updateTable.setItems(rows);
                float totalCost = 0;
                
                long days = Period.between(nsd, ned).getDays();
                for (int i = 0; i < rows.size(); i++) {
                    for (int j = 0; j < days; j++){
                        totalCost += Float.parseFloat(rows.get(i).getCostPerDay());
                        totalCost += Float.parseFloat(rows.get(i).getCostOfBed());
                    }
                }
                
                totalCostUpdated.setVisible(true);
                totalCostUpdated.setText(Float.toString(totalCost));
            } else {
                roomsAvailableMsg.setText("There are no rooms available during this period.");
            }            
        }
    }
    
    @FXML
    public void onSubmit() throws SQLException {
        LocalDate nsd = newStartDate.getValue();
        LocalDate ned = newEndDate.getValue();
        DBM dbm = DBM.sharedDBM();
        dbm.updateReservation(nsd, ned, totalCostUpdated.getText(), reservationID.getText());
        confirmation.setText("Your reservation has been updated");
        confirmation.setVisible(true);
    }
    
    @FXML
    public void onBack() {
        fancyHotelsControl.goToCustomerFunctionalityScreen(username);
    }
}
