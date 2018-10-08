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
public class PopularRoomScreenController extends SceneController implements Initializable {

    @FXML
    private TableView popularRoomTable;
    @FXML
    private TableColumn monthCol;
    @FXML
    private TableColumn topRoomCol;
    @FXML
    private TableColumn locationCol;
    @FXML
    private TableColumn numReservationsCol;
    private String username;
    @FXML
    private Button back;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthCol.setCellValueFactory(new PropertyValueFactory("month"));
        topRoomCol.setCellValueFactory(new PropertyValueFactory("topRoomCategory"));
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        numReservationsCol.setCellValueFactory(new PropertyValueFactory("numReservations"));
        try {
            DBM dbm = DBM.sharedDBM();
            ResultSet results = dbm.getAugPopularRooms();
            List<String> list1 = new ArrayList<>();
            List<String> list2 = new ArrayList<>();
            List<String> list3 = new ArrayList<>();
            while(results.next()) {
                list1.add(results.getString(results.getMetaData().getColumnName(1)));
                list2.add(results.getString(results.getMetaData().getColumnName(2)));
                list3.add(results.getString(results.getMetaData().getColumnName(3)));
            }
            
            ResultSet resultsSept = dbm.getSeptPopularRooms();
            List<String> list4 = new ArrayList<>();
            List<String> list5 = new ArrayList<>();
            List<String> list6 = new ArrayList<>();
            while(resultsSept.next()) {
                list4.add(resultsSept.getString(resultsSept.getMetaData().getColumnName(1)));
                list5.add(resultsSept.getString(resultsSept.getMetaData().getColumnName(2)));
                list6.add(resultsSept.getString(resultsSept.getMetaData().getColumnName(3)));
            }
            
            ObservableList<Category> categories = FXCollections.observableArrayList();
            for (int j = 0; j < list1.size(); j++) {
                Category newCategory = new Category("August", list1.get(j), list2.get(j), list3.get(j));
                categories.add(newCategory);
            }
            
            for (int j = 0; j < list4.size(); j++) {
                Category newCategory = new Category("September", list4.get(j), list5.get(j), list6.get(j));
                categories.add(newCategory);
            }
            
            popularRoomTable.setItems(categories);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void onBack() {
        fancyHotelsControl.goToManagerFunctionalityScreen(username);
    }
}
