/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import javafx.scene.control.CheckBox;

/**
 *
 * @author nkaru_000
 */
public class Update {
    private String roomNum;
    private String roomCat;
    private String numPersons;
    private String costPerDay;
    private String costOfBed;
    private String selectExtra;
    
    public Update(String roomNum, String roomCat, String numPersons, String costPerDay, String costOfBed, String selectExtra) {
        this.roomNum = roomNum;
        this.roomCat = roomCat;
        this.numPersons = numPersons;
        this.costPerDay = costPerDay;
        this.costOfBed = costOfBed;
        this.selectExtra = selectExtra;
    }
    
    public String getRoomNum() {
        return this.roomNum;
    }
    public String getRoomCat() {
        return this.roomCat;
    }
    public String getNumPersons() {
        return this.numPersons;
    }
    public String getCostPerDay() {
        return this.costPerDay;
    }
    public String getCostOfBed() {
        return this.costOfBed;
    }
    public String getSelectExtra() {
        return this.selectExtra;
    }
    
}
