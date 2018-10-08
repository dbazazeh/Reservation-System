/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 *
 * @author nkaru_000
 */
public class FancyHotelsController {
    private FancyHotels system;
    private Stage stage;
    
    public FancyHotelsController(FancyHotels system, Stage stage){
        this.system = system;
        this.stage = stage;
    }
    
    private SceneController extractControllerFromFXML(String fxmlScene,
            Stage myStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlScene));
        try {
            Scene theScene = new Scene(loader.load());
            //System.out.println("happening?1");
            SceneController controller = loader.getController();
            //System.out.println("happening?2");
            controller.setScene(theScene);
            //System.out.println("happening?3");
            controller.setFancyHotelsControl(this);
            //System.out.println("happening?4");
            return controller;
        } catch (IOException e) {
            
        }
        System.out.println("returning null?");
        return null;
    }
    
    public void goToLoginScreen() {
        LoginScreenController control;
        control = (LoginScreenController) extractControllerFromFXML(
                "LoginScreen.fxml", stage);
        stage.setScene(control.getScene());
        stage.setTitle("Fancy Hotels Login");
        stage.show();
    }
    
    public void goToUpdateReservationScreen(String username) {
        UpdateReservationScreenController control;
        control = (UpdateReservationScreenController) extractControllerFromFXML(
                "UpdateReservationScreen.fxml", stage);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Fancy Hotels: Login");
        stage.show();
    }
    
    public void goToCancelReservationScreen(String username) {
        CancelReservationScreenController control;
        control = (CancelReservationScreenController) extractControllerFromFXML(
                "CancelReservationScreen.fxml", stage);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Fancy Hotels: Cancel Reservation");
        stage.show();
    }
    
    public void goToViewReviewScreen(String username) {
        ViewReviewScreenController control;
        control = (ViewReviewScreenController) extractControllerFromFXML(
                "ViewReviewScreen.fxml", stage);
        stage.setScene(control.getScene());
        control.setUsername(username);
        stage.setTitle("Fancy Hotels: View Reviews");
        stage.show();
    }
    
    public void goToGiveReviewScreen(String username) {
        GiveReviewScreenController control;
        control = (GiveReviewScreenController) extractControllerFromFXML(
                "GiveReviewScreen.fxml", stage);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Fancy Hotels: Give Reviews");
        stage.show();
    }
    
    public void goToNewUserScreen() {
        NewUserScreenController control;
        control = ( NewUserScreenController) extractControllerFromFXML(
                "NewUserScreen.fxml", stage);
        stage.setScene(control.getScene());
        stage.setTitle("New User Registeration");
        stage.show();
    }
    public void goToCustomerFunctionalityScreen(String username) {
        CustomerFunctionalityScreenController control;
        control = ( CustomerFunctionalityScreenController) extractControllerFromFXML(
                "CustomerFunctionalityScreen.fxml", stage);
        stage.setScene(control.getScene());
        stage.setTitle("Choose Functionality");
        control.setUsername(username);
        stage.show();
    }
   
   public void goToManagerFunctionalityScreen(String username) {
        ManagerFunctionalityScreenController control;
        control = ( ManagerFunctionalityScreenController) extractControllerFromFXML(
                "ManagerFunctionalityScreen.fxml", stage);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Choose Functionality");
        stage.show();
    }
   
   public void goToSearchRoomsScreen(String username) {
        SearchRoomsScreenController control;
        control = ( SearchRoomsScreenController) extractControllerFromFXML(
                "SearchRoomsScreen.fxml", stage);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Search Rooms");
        stage.show();
    }
   
   public void goToConfirmationScreen(String reservationID, String username) {
        ConfirmationScreenController control;
        control = ( ConfirmationScreenController) extractControllerFromFXML(
                "ConfirmationScreen.fxml", stage);
        control.setResID(reservationID);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Confirmation");
        stage.show();
    }
   
   public void goToPaymentScreen(String username, ObservableList<AvailableRooms> rooms, ArrayList<Integer> rowNums, LocalDate startDate, LocalDate endDate) {
        PaymentInfoScreenController control;
        control = ( PaymentInfoScreenController) extractControllerFromFXML(
                "PaymentInfoScreen.fxml", stage);
        control.setRooms(rooms);
        control.setRowNums(rowNums);
        control.setStartDate(startDate);
        control.setEndDate(endDate);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Payment");
        stage.show();
    }
   
   public void goToMakeAReservation2Screen(String username, ObservableList<AvailableRooms> rooms, ArrayList<Integer> rowNums, LocalDate startDate, LocalDate endDate, String location) {
        MakeAReservation2Controller control;
        control = (MakeAReservation2Controller) extractControllerFromFXML(
                "MakeAReservation2.fxml", stage);
        control.setUsername(username);
        control.setStartDate(startDate);
        control.setEndDate(endDate);
        control.setRowNums(rowNums);
        control.setRooms(rooms);
        control.setLocation(location);
        stage.setScene(control.getScene());
        stage.setTitle("Make a Reservation");
        stage.show();
    }
   
    public void goToReservationReportScreen(String username) {
        ReservationReportScreenController control;
        control = (ReservationReportScreenController) extractControllerFromFXML(
                "ReservationReportScreen.fxml", stage);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Fancy Hotels: View Reservation Report");
        stage.show();
    }
    
    public void goToPopularRoomScreen(String username) {
        PopularRoomScreenController control;
        control = (PopularRoomScreenController) extractControllerFromFXML(
                "PopularRoomScreen.fxml", stage);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Fancy Hotels: View Reservation Report");
        stage.show();
    }
    
    public void goToRevenueReportScreen(String username) {
        RevenueReportScreenController control;
        control = (RevenueReportScreenController) extractControllerFromFXML(
                "RevenueReportScreen.fxml", stage);
        control.setUsername(username);
        stage.setScene(control.getScene());
        stage.setTitle("Fancy Hotels: View Revenue Report");
        stage.show();
    }
    
    public void goToReservationScreen(String username, ResultSet rs, LocalDate startDate, LocalDate endDate, String location){
        MakeAReservation1Controller control;
        control = (MakeAReservation1Controller) extractControllerFromFXML(
           "MakeAReservation1.fxml", stage);
        control.setRS(rs);
        control.setStartDate(startDate);
        control.setEndDate(endDate);
        control.setUsername(username);
        control.setLocation(location);
        stage.setScene(control.getScene());
        stage.setTitle("Reservation");
        stage.show();
   
   }
}
