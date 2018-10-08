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
public class RevenueReportScreenController extends SceneController implements Initializable {

    @FXML
    private TableView revenueTable;
    @FXML
    private TableColumn monthCol;
    @FXML
    private TableColumn locationCol;
    @FXML
    private TableColumn revenueCol;
    @FXML
    private Button back;
    
    private String username;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthCol.setCellValueFactory(new PropertyValueFactory("month"));
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        revenueCol.setCellValueFactory(new PropertyValueFactory("revenue"));
        try {
            DBM dbm = DBM.sharedDBM();
            ResultSet results = dbm.getAugRevenueReport();
            List<String> list1 = new ArrayList<>();
            List<String> list2 = new ArrayList<>();
            while(results.next()) {
                list1.add(results.getString(results.getMetaData().getColumnName(1)));
                list2.add(results.getString(results.getMetaData().getColumnName(2)));
            }
            
            ResultSet resultsSept = dbm.getSeptRevenueReport();
            List<String> list3 = new ArrayList<>();
            List<String> list4 = new ArrayList<>();
            while(resultsSept.next()) {
                list3.add(resultsSept.getString(resultsSept.getMetaData().getColumnName(1)));
                list4.add(resultsSept.getString(resultsSept.getMetaData().getColumnName(2)));
            }
            
            ObservableList<Revenue> revenues = FXCollections.observableArrayList();
            for (int j = 0; j < list1.size(); j++) {
                Revenue newRevenue = new Revenue("August", list1.get(j), list2.get(j));
                revenues.add(newRevenue);
            }
            
            for (int j = 0; j < list3.size(); j++) {
                Revenue newRevenue = new Revenue("September", list3.get(j), list4.get(j));
                revenues.add(newRevenue);
            }
            
            revenueTable.setItems(revenues);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }    
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @FXML
    public void onBack() {
        fancyHotelsControl.goToManagerFunctionalityScreen(username);
    }
    
}
