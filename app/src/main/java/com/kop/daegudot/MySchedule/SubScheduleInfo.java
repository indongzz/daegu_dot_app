package com.kop.daegudot.MySchedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SubScheduleInfo  implements Parcelable {
    private String date;
    private ArrayList<String> address;
    
    public SubScheduleInfo() {
    
    }
    
    protected SubScheduleInfo(Parcel in) {
        date = in.readString();
        address = in.readArrayList(String.class.getClassLoader());
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
    }
}
