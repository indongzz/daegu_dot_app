package com.kop.daegudot.Network.Schedule;

import android.os.Parcel;
import android.os.Parcelable;

import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.Network.Map.Place;

import java.time.LocalDate;

public class SubScheduleResponse implements Comparable<SubScheduleResponse>, Parcelable {
    public long id;
    public String date;
    public Place placesResponseDto;
    
    public SubScheduleResponse() {
    
    }
    
    protected SubScheduleResponse(Parcel in) {
        id = in.readLong();
        date = in.readString();
        placesResponseDto = in.readParcelable(Place.class.getClassLoader());
    }
    
    public static final Creator<SubScheduleResponse> CREATOR = new Creator<SubScheduleResponse>() {
        @Override
        public SubScheduleResponse createFromParcel(Parcel in) {
            return new SubScheduleResponse(in);
        }
        
        @Override
        public SubScheduleResponse[] newArray(int size) {
            return new SubScheduleResponse[size];
        }
    };
    
    @Override
    public int compareTo(SubScheduleResponse o) {
        LocalDate date1 = LocalDate.parse(this.date);
        LocalDate date2 = LocalDate.parse(o.date);
        
        if (date1.isBefore(date2)) {
            return -1;
        } else if (date1.isAfter(date2)) {
            return 1;
        } else {
            return 0;
        }
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    
        dest.writeLong(id);
        dest.writeString(date);
        dest.writeParcelable(placesResponseDto, flags);
    }
}
