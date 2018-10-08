/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fancyhotels;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Dana
 */
public class PaymentInfoScreenController extends SceneController implements Initializable {

    @FXML
    private Button saveCard;
    @FXML
    private Button deleteCard;
    @FXML
    private Label errorMessage4;
    @FXML
    private TextField cardName;
    @FXML
    private TextField cardNumber;
    @FXML
    private TextField cvv;
    
    @FXML
    private ChoiceBox cardNumberDelete;
    
    @FXML
    private TextField expField;
    @FXML
    private Button back;
    
    String username;
    private LocalDate endDate;
    private LocalDate startDate;
    private ArrayList<Integer> rowNums;
    private ObservableList<AvailableRooms> rooms;
    private String location;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
                     
        saveCard.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Calendar now = Calendar.getInstance();
                String expiryDate = expField.getText();
                
                int m = 0;
                int y = 0;
                
                
                 
                if (expiryDate.length() == 7){
                    
                String month = expiryDate.substring(0,2);
                String year = expiryDate.substring(3,7);
                m = Integer.parseInt(month);
                y = Integer.parseInt(year);
                
                
                }if (cardName.getText().isEmpty() || cardNumber.getText().isEmpty() || cvv.getText().isEmpty()
                         ||expField.getText().isEmpty())
                {
                    errorMessage4.setText("Fields Can't be empty");
                 
                } 
                 
                 
                else if (expiryDate.length() != 7){
                errorMessage4.setText("wrong expiry date format");
                
                }
                else if (!(expiryDate.charAt(2) == '/')){
                errorMessage4.setText("wrong expiry date format");
                
                }
                 else if (cardNumber.getText().length() < 14  || cardNumber.getText().length() > 16  ){
                errorMessage4.setText("Invalid card Number");
                
                } 
                 
                 else if (cvv.getText().length() < 3 || cvv.getText().length() > 4 ){
                errorMessage4.setText("Invalid CVV");
                
                } else if (m > 12 || m < 1) {
                    
                errorMessage4.setText("Invalid Month");
                
                }
                 else if (y >9999 || y < 1000) {
                    
                errorMessage4.setText("Invalid Year");
                
                }
                 else if (y <= now.get(Calendar.YEAR) && m < now.get(Calendar.MONTH) + 1) {
                    
                errorMessage4.setText("Card Expired");
               
                }
                
                 else 
                 {  boolean validCard = true;
                    boolean validCVV = true;
                     char c = 'a';
                     for (int i = 0; i < cardNumber.getText().length(); i++){
                         
                         c = cardNumber.getText().charAt(i);
                         if (c < 48 || c >57){
                             validCard = false;
                         
                         }
                     
                     }
                     for (int i = 0; i < cvv.getText().length(); i++){
                         
                         c = cvv.getText().charAt(i);
                         if (c < 48 || c >57){
                             validCVV = false;
                         
                         }
                     
                     }
                     if (!validCard){
                         errorMessage4.setText("Card can only have digits");
                     
                     }
                     else if (!validCVV){
                     errorMessage4.setText("CVV can only have digits");
                     }
                     else {
                     
                     try{
                     String month1 = expiryDate.substring(0,2);
                     String year1 = expiryDate.substring(3,7);
                     String ExpDate1 = "15." + month1 + "." + year1;
                     DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
                    
                     LocalDate expDate1 =  LocalDate.parse(ExpDate1, germanFormatter);
                     int cvv2 = Integer.parseInt(cvv.getText());
                     
                  DBM dbm = DBM.sharedDBM();   
                  dbm.insertCard(cardName.getText(), cardNumber.getText(),  cvv2, expDate1,  username);
                  fancyHotelsControl.goToMakeAReservation2Screen(username, rooms, rowNums, startDate, endDate, location);
                     }
                      catch (SQLException e){}
                 }  
                 }
               
            }
        });
        
        deleteCard.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent e) {
         String s = (String)cardNumberDelete.getValue();
         System.out.println(s);
        if (s != null && !s.isEmpty()){
        
        try {
            DBM dbm = DBM.sharedDBM();
            boolean canDelete = dbm.checkIfCanDelete(s);
            
            if (canDelete){
                System.out.println("true");
            dbm.deleteCard(s);
            System.out.println("Deleted");
            fancyHotelsControl.goToMakeAReservation2Screen(username, rooms, rowNums, startDate, endDate, location);
            }
            else {
                System.out.println("NotDeleted");
                errorMessage4.setText("Card could not be deleted");
                        
                        }
        
        }
        catch (SQLException e2){}
            
        }
        else {
            errorMessage4.setText("Select card first");
        }
        
        
        
        
    }
        });
        // TODO
    } 
    
    @FXML
    public void onBack() {
        fancyHotelsControl.goToMakeAReservation2Screen(username, rooms, rowNums, startDate, endDate, location);
    }
    
    public void setUsername(String s){
    
        this.username = s;
        try {
            DBM dbm = DBM.sharedDBM();
            
                String[] cards =  dbm.getCards(username);
               
                for (int i =0; i< cards.length; i++){
                    
                cardNumberDelete.getItems().add(cards[i]);
                }
               
        }
         catch (SQLException e){
             System.out.println(e.getMessage());
         }
    
    }

    void setRooms(ObservableList<AvailableRooms> rooms) {
        this.rooms = rooms;
    }

    void setRowNums(ArrayList<Integer> rowNums) {
        this.rowNums = rowNums;
    }

    void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
}
