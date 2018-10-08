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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * FXML Controller class
 *
 * @author Dana
 */
public class MakeAReservation1Controller extends SceneController implements Initializable {

    
    @FXML
    private TableView  tableReserve;
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
    private TableColumn selectRoom;
    @FXML
    private Button checkD;
    private ResultSet rs;
    @FXML
    private Button find;
    private ArrayList<Integer> rowNums;
    private ObservableList<AvailableRooms> rooms;
    private LocalDate startDate;
    private LocalDate endDate;
    private String username;
    private String location;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //makeTable();
    }    
    
    public void setRS(ResultSet rs){
        this.rs = rs;
    }
    
    @FXML
    public void onFind() {
        rowNums = new ArrayList();
        roomNumber.setCellValueFactory(new PropertyValueFactory("RoomNo"));
        roomCategory.setCellValueFactory(new PropertyValueFactory("RoomCategory"));
        personsAllowed.setCellValueFactory(new PropertyValueFactory("NumOfPersons"));
        costForDay.setCellValueFactory(new PropertyValueFactory("costPerDay"));
        costForExtraBed.setCellValueFactory(new PropertyValueFactory("costOfBed"));
        
        tableReserve.setEditable(true);
        selectRoom.setCellFactory(TextFieldTableCell.forTableColumn());
        
        selectRoom.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<String, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<String, String> t) {
                System.out.println(t.getNewValue());
                if (t.getNewValue().equals("1")){
                    rowNums.add(t.getTablePosition().getRow());
                    System.out.println("is this happening?3");
                }
                System.out.println("is this happening?2");
            }
        });

        
        tableReserve.getColumns().remove(selectRoom);
        tableReserve.getColumns().add(selectRoom);
        
        
        
        try {
            List <String> l1 = new ArrayList();
            List <String> l2 = new ArrayList();
            List <String> l3 = new ArrayList();
            List <String> l4 = new ArrayList();
            List <String> l5 = new ArrayList();
            
            while (rs.next()){
                l1.add(rs.getString(rs.getMetaData().getColumnName(1)));
                l2.add(rs.getString(rs.getMetaData().getColumnName(2)));
                l3.add(rs.getString(rs.getMetaData().getColumnName(3)));
                l4.add(rs.getString(rs.getMetaData().getColumnName(4)));
                l5.add(rs.getString(rs.getMetaData().getColumnName(5)));
            }
            
            rooms = FXCollections.observableArrayList();
            //CheckBox [] selectRoom = new CheckBox[l1.size()];
            
            for (int i = 0; i < l1.size(); i++){
                //System.out.println(l4.get(i));
                AvailableRooms newRoom = new AvailableRooms(l1.get(i),l2.get(i),l3.get(i), l4.get(i), l5.get(i), false);
                rooms.add(newRoom);
            }
            //System.out.println(rooms.get(0).getCostPerDay());
            tableReserve.setItems(rooms);
            
        } catch (SQLException e) {
        
        }
    }
    
    @FXML
    public void onCheckDetails() {
        if(rowNums.size() == 0) {
            //set error message - make them pick a room
        }
        fancyHotelsControl.goToMakeAReservation2Screen(username, rooms, rowNums, startDate, endDate, location);
    }

    void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
}
