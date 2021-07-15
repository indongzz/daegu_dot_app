package com.kop.daegudot.MySchedule;

import android.os.Parcel;
import android.os.Parcelable;

import com.kop.daegudot.Network.Schedule.SubScheduleResponse;

import java.time.LocalDate;
import java.util.ArrayList;

public class DateSubSchedule implements Parcelable {
    public String date;
    public ArrayList<SubScheduleResponse> subScheduleList;
    
    public DateSubSchedule() {
    
    }
    
    protected DateSubSchedule(Parcel in) {
        date = in.readString();
        subScheduleList = in.readArrayList(SubScheduleResponse.class.getClassLoader());
    }
    
    public static final Creator<DateSubSchedule> CREATOR = new Creator<DateSubSchedule>() {
        @Override
        public DateSubSchedule createFromParcel(Parcel in) {
            return new DateSubSchedule(in);
        }
        
        @Override
        public DateSubSchedule[] newArray(int size) {
            return new DateSubSchedule[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeList(subScheduleList);
    }
}
