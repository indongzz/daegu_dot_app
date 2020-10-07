package com.kop.daegudot.MySchedule;

import java.util.ArrayList;

public class ItineraryInfo {
    private String date;
    private ArrayList<String> address;
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public ArrayList<String> getAddress() {
        return address;
    }
    
    public void setAddress(ArrayList<String> address) {
        this.address = address;
    }
    
    public String getAddressString() {
        String text = "";
        
        for (String t : address) {
            text += t;
        }
        
        return text;
    }
}
