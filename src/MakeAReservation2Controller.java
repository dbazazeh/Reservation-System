/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fancyhotels;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * FXML Controller class
 *
 * @author Dana
 */
public class MakeAReservation2Controller extends SceneController implements Initializable {

    @FXML
    private ChoiceBox selectCard;
    @FXML
    private Hyperlink addCardLink;
    @FXML
    private Button submitReservation;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label endDateLabel;
    @FXML
    private Label totalCostLabel;
    @FXML 
    private TableColumn roomNumber;
    @FXML 
    private TableColumn roomCategory;
    @FXML 
    private TableColumn personsAllowed;
    @FXML 
    private TableColumn costForDay;
    @FXML 
    private TableColumn costForExtraBed;
    @FXML 
    private TableView selectTable;
    @FXML 
    private TableColumn hasExtraBed;
    @FXML
    private Label error5;
    @FXML
    private Button updateCost;
    
    private ArrayList<Integer> rowHasBed;
    private ArrayList<Integer> rowNums;
    private ObservableList<AvailableRooms> rooms;
    private LocalDate startDate;
    private LocalDate endDate;
    private float totalCost;
    private String username;
    private String location;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    public void onSubmit() {
        String cardNumber = (String) selectCard.getValue();
        if (cardNumber == null){
            error5.setText("Select Card First");
        } else {
            try {
                System.out.println("happening?1");
                DBM dbm = DBM.sharedDBM();
                int resID = dbm.getNewID();
                System.out.println("happening?2");
                float newCost = 0;
                ObservableList<AvailableRooms> newList = FXCollections.observableArrayList();
                for (int i = 0; i < rowNums.size(); i++){
                    newList.add(rooms.get(rowNums.get(i)));
                }
                System.out.println("happening?3");
                long days = Period.between(startDate, endDate).getDays();
                for (int i = 0; i < newList.size(); i++) {
                    for (int j = 0; j < days; j++){
                        newCost += Float.parseFloat(newList.get(i).getCostPerDay());
                        if (newList.get(i).getHasBed()) {
                            newCost += Float.parseFloat(newList.get(i).getCostOfBed());
                        }
                    }
                }   
                this.totalCost = newCost;
                totalCostLabel.setText(String.format("%.2f", totalCost));
                System.out.println(startDate);
                System.out.println(endDate);
                dbm.insertNewRes(resID, this.startDate, this.endDate, this.totalCost, this.username, cardNumber);
                System.out.println("happening?4");       
                for (int i = 0; i < newList.size() ; i++){
                    int roomNum = Integer.parseInt(newList.get(i).getRoomNo());
                    String Location = this.location;
                    boolean extraBed = true;
        
                    dbm.insertNewResHasRoom(resID, roomNum, location, extraBed);
                    System.out.println("happening?5");
                }
                System.out.println("happening?6");
                String resId2 = Integer.toString(resID);
                System.out.println("happening?7");
                fancyHotelsControl.goToConfirmationScreen(resId2, username);
            } catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void setUsername(String username) {
        this.username = username;
        try {
            DBM dbm = DBM.sharedDBM();
            
                String[] cards =  dbm.getCards(username);
               
                for (int i =0; i< cards.length; i++){
                    
                selectCard.getItems().add(cards[i]);
                }
               
        }
         catch (SQLException e){}
    }
    
    @FXML
    public void onAddCard() {
        fancyHotelsControl.goToPaymentScreen(username, rooms, rowNums, startDate, endDate);
    }
    
    public void setRowNums(ArrayList<Integer> rowNums) {
        this.rowNums = rowNums;
    }
    
    public void setRooms(ObservableList<AvailableRooms> rooms) {
        this.rooms = rooms;
        
        roomNumber.setCellValueFactory(new PropertyValueFactory("RoomNo"));
        roomCategory.setCellValueFactory(new PropertyValueFactory("RoomCategory"));
        personsAllowed.setCellValueFactory(new PropertyValueFactory("NumOfPersons"));
        costForDay.setCellValueFactory(new PropertyValueFactory("costPerDay"));
        costForExtraBed.setCellValueFactory(new PropertyValueFactory("costOfBed"));
        
        selectTable.setEditable(true);
        hasExtraBed.setCellFactory(TextFieldTableCell.forTableColumn());
        hasExtraBed.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<AvailableRooms, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<AvailableRooms, String> t) {
                if (t.getNewValue().equals("1")){
                    t.getRowValue().setHasBed(true);
                }
            }

        });
        
        selectTable.getColumns().remove(hasExtraBed);
        selectTable.getColumns().add(hasExtraBed);
        //int size = rooms.size();
        ObservableList<AvailableRooms> newList = FXCollections.observableArrayList();
        for (int i = 0; i < rowNums.size(); i++){
            newList.add(rooms.get(rowNums.get(i)));
        }
        
        selectTable.setItems(newList);
        
        long days = Period.between(startDate, endDate).getDays();
        for (int i = 0; i < newList.size(); i++) {
            for (int j = 0; j < days; j++){
                this.totalCost += Float.parseFloat(newList.get(i).getCostPerDay());
                if (newList.get(i).getHasBed()) {
                    this.totalCost += Float.parseFloat(newList.get(i).getCostOfBed());
                }
            }
        }
        totalCostLabel.setText(String.format("%.2f", totalCost));
        
    }

    void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        startDateLabel.setText(startDate.toString());
    }

    void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        endDateLabel.setText(endDate.toString());
    }
    
    @FXML
    public void onUpdate() {
        float newCost = 0;
        ObservableList<AvailableRooms> newList = FXCollections.observableArrayList();
        for (int i = 0; i < rowNums.size(); i++){
            newList.add(rooms.get(rowNums.get(i)));
        }
        long days = Period.between(startDate, endDate).getDays();
        for (int i = 0; i < newList.size(); i++) {
            for (int j = 0; j < days; j++){
                newCost += Float.parseFloat(newList.get(i).getCostPerDay());
                if (newList.get(i).getHasBed()) {
                    newCost += Float.parseFloat(newList.get(i).getCostOfBed());
                }
            }
        }
        this.totalCost = newCost;
        totalCostLabel.setText(String.format("%.2f", totalCost));
    }

    void setLocation(String location) {
        this.location = location;
    }
    
}
