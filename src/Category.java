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
public class Category {
    private String month;
    private String topRoomCategory;
    private String location;
    private String numReservations;
    
    public Category(String month, String topRoomCategory, String location, String numReservations) {
        this.month = month;
        this.topRoomCategory = topRoomCategory;
        this.location = location;
        this.numReservations = numReservations;
    }
    
    public String getMonth() {
        return this.month;
    }
    public String getTopRoomCategory() {
        return this.topRoomCategory;
    }
    public String getLocation() {
        return this.location;
    }
    public String getNumReservations() {
        return this.numReservations;
    }
}
