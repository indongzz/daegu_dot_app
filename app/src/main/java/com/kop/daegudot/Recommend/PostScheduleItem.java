package com.kop.daegudot.Recommend;

import java.util.ArrayList;

public class PostScheduleItem {
    private int day;
    private ArrayList<String> placeName;
    
    public int getDay() {
        return day;
    }
    
    public void setDay(int day) {
        this.day = day;
    }
    
    public ArrayList<String> getPlaceName() {
        return placeName;
    }
    
    public void setPlaceName(ArrayList<String> placeName) {
        this.placeName = placeName;
    }
}
