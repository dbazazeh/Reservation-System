/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author nkaru_000
 */
public class GiveReviewScreenController extends SceneController implements Initializable {

    @FXML
    private ChoiceBox hotelLocation;
    @FXML
    private ChoiceBox rating;
    @FXML
    private TextArea comment;
    @FXML
    private Button submit;
    @FXML
    private Label allFieldsError;
    @FXML
    private Button back;
    
    private String username;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hotelLocation.getItems().add("Atlanta");
        hotelLocation.getItems().add("Charlotte");
        hotelLocation.getItems().add("Orlando");
        hotelLocation.getItems().add("Miami");
        hotelLocation.getItems().add("Savannah");
        rating.getItems().add("Excellent");
        rating.getItems().add("Good");
        rating.getItems().add("Bad");
        rating.getItems().add("Very bad");
        rating.getItems().add("Neutral");
        allFieldsError.setVisible(false);
    }    
    
    @FXML
    public void onSubmit() throws SQLException {
        String location = (String) hotelLocation.getValue();
        String rate = (String) rating.getValue();
        String cmt = (String) comment.getText();
        if (location == null || rate == null) {
            allFieldsError.setText("Please select both a location and rating");
            allFieldsError.setVisible(true);
        } else {
            allFieldsError.setVisible(false);
            DBM dbm = DBM.sharedDBM();
            dbm.addReview(cmt, rate, location , username);
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
