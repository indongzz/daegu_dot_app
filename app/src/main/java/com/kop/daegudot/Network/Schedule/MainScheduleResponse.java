package com.kop.daegudot.Network.Schedule;

import android.os.Parcel;
import android.os.Parcelable;

public class MainScheduleResponse implements Parcelable {
    public String startDate;
    public String endDate;
    public long id;
    
    public MainScheduleResponse() {
    
    }
    
    public MainScheduleResponse(Parcel in) {
        startDate = in.readString();
        endDate = in.readString();
        id = in.readLong();
    }
    
    
    public static final Creator<MainScheduleResponse> CREATOR = new Creator<MainScheduleResponse>() {
        @Override
        public MainScheduleResponse createFromParcel(Parcel in) {
            return new MainScheduleResponse(in);
        }
        
        @Override
        public MainScheduleResponse[] newArray(int size) {
            return new MainScheduleResponse[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeLong(id);
    }
}
