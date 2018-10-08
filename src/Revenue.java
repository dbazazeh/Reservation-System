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
public class Revenue {
    private String month;
    private String location;
    private String revenue;
    
    public Revenue(String month, String location, String revenue) {
        this.month = month;
        this.location = location;
        this.revenue = revenue;
    }
    
    public String getMonth() {
        return this.month;
    }
    public String getLocation() {
        return this.location;
    }
    public String getRevenue() {
        return this.revenue;
    }
}
