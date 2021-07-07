package com.kop.daegudot.MySchedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SubScheduleInfo  implements Parcelable {
    private String date;
    private ArrayList<String> address;
    private int mainTag;
    private long placeNum;
    private ArrayList<String> placeName;
    private long subId;
    
    public SubScheduleInfo() {
    
    }
    
    protected SubScheduleInfo(Parcel in) {
        date = in.readString();
        address = in.readArrayList(String.class.getClassLoader());
        placeName = in.readArrayList(String.class.getClassLoader());
    }
    
    public static final Creator<SubScheduleInfo> CREATOR = new Creator<SubScheduleInfo>() {
        @Override
        public SubScheduleInfo createFromParcel(Parcel in) {
            return new SubScheduleInfo(in);
        }

        @Override
        public SubScheduleInfo[] newArray(int size) {
            return new SubScheduleInfo[size];
        }
    };
    
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
    
    public long getPlaceNum() {
        return placeNum;
    }
    
    public void setPlaceNum(long placeNum) {
        this.placeNum = placeNum;
    }
    
    public ArrayList<String> getPlaceName() {
        return placeName;
    }
    
    public void setPlaceName(ArrayList<String> placeName) {
        this.placeName = placeName;
    }
    
    public long getSubId() {
        return subId;
    }
    
    public void setSubId(long subId) {
        this.subId = subId;
    }
    
    
    public String getAddressString() {
        String text = "";
        
        for (String t : address) {
            text += t;
        }
        
        return text;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeList(address);
        dest.writeList(placeName);
    }
}
