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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class ViewReviewScreenController extends SceneController implements Initializable {

    @FXML
    private ChoiceBox hotelLocation;
    @FXML
    private Button checkReviews;
    @FXML 
    private Label noLocationError;
    @FXML
    private TableColumn ratingCol;
    @FXML
    private TableView reviewTable;
    @FXML
    private TableColumn<?, ?> commentCol;
    @FXML
    private Button back;
    private String username;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ratingCol.setCellValueFactory(new PropertyValueFactory("rating"));
        commentCol.setCellValueFactory(new PropertyValueFactory("comment"));
        hotelLocation.getItems().add("Atlanta");
        hotelLocation.getItems().add("Charlotte");
        hotelLocation.getItems().add("Orlando");
        hotelLocation.getItems().add("Miami");
        hotelLocation.getItems().add("Savannah");
        noLocationError.setVisible(false);
    }    
    
    @FXML
    public void onCheckReviews() throws SQLException {
        String chosen = (String) hotelLocation.getValue();
        if (chosen == null) {
            noLocationError.setText("Please select a city");
            noLocationError.setVisible(true);
        } else {
            //populate table with sql
            DBM dbm = DBM.sharedDBM();
            ResultSet results = dbm.getReviews(chosen);
            List<String> list1 = new ArrayList<>();
            List<String> list2 = new ArrayList<>();
            while(results.next()) {
                list1.add(results.getString("Rating"));
                list2.add(results.getString("Comment"));
            }
            
            ObservableList<Review> reviews = FXCollections.observableArrayList();
            for (int j = 0; j < list1.size(); j++) {
                Review newReview = new Review(list1.get(j), list2.get(j));
                reviews.add(newReview);
            }
            reviewTable.setItems(reviews);
        }
    }
    
    @FXML
    public void onBack() {
        fancyHotelsControl.goToCustomerFunctionalityScreen(username);
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}
