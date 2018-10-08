/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

/**
 *
 * @author nkaru_000
 */
public class Reservation {
    private String month;
    private String location;
    private String numReservations;
    
    public Reservation(String month, String location, String numReservations) {
        this.month = month;
        this.location = location;
        this.numReservations = numReservations;
    }
    
    public String getMonth() {
        return this.month;
    }
    public String getLocation() {
        return this.location;
    }
    public String getNumReservations() {
        return this.numReservations;
    }
}
