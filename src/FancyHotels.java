/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author nkaru_000
 */
public class FancyHotels extends Application {
    private FancyHotelsController fancyHotelsControl;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        fancyHotelsControl = new FancyHotelsController(this, primaryStage);
        fancyHotelsControl.goToLoginScreen();
       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
