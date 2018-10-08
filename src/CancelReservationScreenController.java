/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
public class CancelReservationScreenController extends SceneController implements Initializable {
    @FXML
    private TextField reservationID;
    @FXML
    private Label allFieldsError;
    @FXML
    private Button search;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Label totalCost;
    @FXML
    private Label cancellationDate;
    @FXML
    private Label refundAmount;
    @FXML 
    private Button cancel;
    @FXML
    private Button back;
    private String username;
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
    private TableView cancelTable;
    @FXML 
    private Label confirmation;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        totalCost.setVisible(false);
        cancellationDate.setVisible(false);
        refundAmount.setVisible(false);
        startDate.setDisable(true);
        endDate.setDisable(true);
        allFieldsError.setVisible(false);
        confirmation.setVisible(false);
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @FXML
    public void onSearch() throws SQLException {
        if (reservationID.getText().equals("") || !reservationID.getText().matches("[0-9]+")
                || reservationID.getText().length() != 5) {
            allFieldsError.setText("Please enter a valid reservation ID");
            allFieldsError.setVisible(true);
        } else {            
            int ID = Integer.parseInt(reservationID.getText());
            DBM dbm = DBM.sharedDBM();
            if (dbm.verifyID(ID, username)) {
                ResultSet cancels = dbm.getCancellationInfo(reservationID.getText(), this.username);
                
                if (!cancels.next()) {
                    allFieldsError.setVisible(true);
                    allFieldsError.setText("You have no reservation with that ID");
                    return;
                }
                cancels.beforeFirst();
                allFieldsError.setVisible(false);
                startDate.setDisable(false);
                endDate.setDisable(false);
                
                cancels.next();
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                String strStart = cancels.getString("StartDate");
                LocalDate startLocalDate = LocalDate.parse(strStart, formatter);
                startDate.setValue(startLocalDate);
                
                String strEnd = cancels.getString("EndDate");
                LocalDate endLocalDate = LocalDate.parse(strEnd, formatter);
                endDate.setValue(endLocalDate);
                
                cancels.beforeFirst();
                
                roomNum.setCellValueFactory(new PropertyValueFactory("roomNum"));
                roomCat.setCellValueFactory(new PropertyValueFactory("roomCat"));
                numPersons.setCellValueFactory(new PropertyValueFactory("numPersons"));
                costPerDay.setCellValueFactory(new PropertyValueFactory("costPerDay"));
                costOfBed.setCellValueFactory(new PropertyValueFactory("costOfBed"));
                selectExtra.setCellValueFactory(new PropertyValueFactory("selectExtra"));
                
                List<String> list1 = new ArrayList<>();
                List<String> list2 = new ArrayList<>();
                List<String> list3 = new ArrayList<>();
                List<String> list4 = new ArrayList<>();
                List<String> list5 = new ArrayList<>();
                List<String> list6 = new ArrayList<>();
                
                while(cancels.next()) {
                    list1.add(cancels.getString(cancels.getMetaData().getColumnName(4)));
                    list2.add(cancels.getString(cancels.getMetaData().getColumnName(5)));
                    list3.add(cancels.getString(cancels.getMetaData().getColumnName(6)));
                    list4.add(cancels.getString(cancels.getMetaData().getColumnName(7)));
                    list5.add(cancels.getString(cancels.getMetaData().getColumnName(8)));
                    list6.add(cancels.getString(cancels.getMetaData().getColumnName(9)));
                }   
                
                ObservableList<Update> cancelRows = FXCollections.observableArrayList();
                for (int j = 0; j < list1.size(); j++) {
                    Update newReservation = new Update(list1.get(j), list2.get(j), list3.get(j), list4.get(j), list5.get(j), list6.get(j));
                    cancelRows.add(newReservation);
                }
                //System.out.println(cancelRows.size());
                cancelTable.setItems(cancelRows);
                
                totalCost.setVisible(true);
                cancellationDate.setVisible(true);
                refundAmount.setVisible(true);
                
                float totalPrice = 0;
                
                long days = Period.between(startDate.getValue(),endDate.getValue()).getDays();
                //System.out.println(days);
                for (int i = 0; i < cancelRows.size(); i++) {
                    for (int j = 0; j < days; j++){
                        totalPrice += Float.parseFloat(cancelRows.get(i).getCostPerDay());
                        totalPrice += Float.parseFloat(cancelRows.get(i).getCostOfBed());
                    }
                }
                totalCost.setText(String.format("%.2f", totalPrice));
                
                cancellationDate.setText(LocalDate.now().toString());
                
                float amtToBeRefunded = 0;
                if (LocalDate.now().equals(startDate.getValue().minusDays(1))
                        || LocalDate.now().equals(startDate.getValue())) {
                    amtToBeRefunded = 0;
                } else if (LocalDate.now().equals(startDate.getValue().minusDays(2))
                        || LocalDate.now().equals(startDate.getValue().minusDays(3))) {
                    amtToBeRefunded = (float) (totalPrice * 0.8);
                } else if (LocalDate.now().isBefore(startDate.getValue().minusDays(3))) {
                    amtToBeRefunded = totalPrice;
                }
                refundAmount.setText(String.format("%.2f", amtToBeRefunded));
            }
            allFieldsError.setVisible(false);
        }   
    }
    
    @FXML
    public void onCancel() throws SQLException {
        DBM dbm = DBM.sharedDBM();
        float updatedTotalCost = Float.parseFloat(totalCost.getText()) - Float.parseFloat(refundAmount.getText());
        dbm.cancelReservation(updatedTotalCost, reservationID.getText());
        confirmation.setText("Your reservation has been cancelled");
        confirmation.setVisible(true);
        reservationID.setDisable(true);
        startDate.setDisable(true);
        endDate.setDisable(true);
    }
    
    @FXML
    public void onBack() {
        fancyHotelsControl.goToCustomerFunctionalityScreen(username);
    }
    
}
