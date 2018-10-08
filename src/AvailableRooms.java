/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fancyhotels;

import javafx.scene.control.CheckBox;

/**
 *
 * @author Dana
 */
public class AvailableRooms {
    
    private String RoomNo;
    
    private String RoomCategory;
 
    private String NumOfPersons;
  
    private String costPerDay;
  
    private String costOfBed;
    
    private boolean hasBed;
    
    //private String selectRoom;
    
    AvailableRooms(String RoomNo, String RoomCategory, String NumOfPersons, String costPerDay, String costOfBed, boolean hasBed){
        this.RoomNo = RoomNo;
        this.RoomCategory = RoomCategory;
        this.NumOfPersons = NumOfPersons;
        this.costOfBed = costOfBed;
        this.costPerDay = costPerDay;
        this.hasBed = hasBed;
        //this.selectRoom = selectRoom;
    }
    
    public String getRoomCategory(){
        return RoomCategory; 
    }
    public String getRoomNo(){
        return RoomNo;    
    }
    
    public String getNumOfPersons(){
        return NumOfPersons;   
    }
    
    public String getCostPerDay(){
        return this.costPerDay;   
    }
    
    public String getCostOfBed(){
        return this.costOfBed;   
    }
    
    public boolean getHasBed(){
        return this.hasBed;    
    }
    
    public void setHasBed(boolean b) {
        this.hasBed = b;
    }
}
