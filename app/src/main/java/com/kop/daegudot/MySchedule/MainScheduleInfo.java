package com.kop.daegudot.MySchedule;

import android.os.Parcel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MainScheduleInfo implements Comparable<MainScheduleInfo> { //, Parcelable{
    private String mFirstDate;
    private String mLastDate;
    private LocalDate mFirstLocalDate;
    private LocalDate mLastLocalDate;
    private int mDDate;
    
    public MainScheduleInfo() {

    }

    protected MainScheduleInfo(Parcel in) {
        mFirstDate = in.readString();
        mLastDate = in.readString();
        mFirstLocalDate = (LocalDate) in.readValue(LocalDate.class.getClassLoader());
        mLastLocalDate = (LocalDate) in.readValue(LocalDate.class.getClassLoader());
        mDDate = in.readInt();
    }

//    public static final Creator<DateInfo> CREATOR = new Creator<DateInfo>() {
//        @Override
//        public DateInfo createFromParcel(Parcel in) {
//            return new DateInfo(in);
//        }
//
//        @Override
//        public DateInfo[] newArray(int size) {
//            return new DateInfo[size];
//        }
//    };
    
    public String getmFirstDate() {
        return mFirstDate;
    }
    
    public void setmFirstDate(String mFirstDate) {
        this.mFirstDate = mFirstDate;
    }
    
    public String getmLastDate() {
        return mLastDate;
    }
    
    public void setmLastDate(String mLastDate) {
        this.mLastDate = mLastDate;
    }
    
    public int getmDDate() {
        return mDDate;
    }
    
    public void setmDDate() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yy.MM.dd");
        mFirstLocalDate = LocalDate.parse(mFirstDate, format);
        mLastLocalDate = LocalDate.parse(mLastDate, format);
        LocalDate today = LocalDate.now();
        mDDate = (int) ChronoUnit.DAYS.between(today, mFirstLocalDate);
    }
    
    public String getTextString() {
        // 년도 제외하고 월 일만 추출
        return mFirstDate.substring(3) + " ~ " + mLastDate.substring(3) + " / " + "D-" + mDDate;
    }
    
    
    public LocalDate[] getDateArray() {
        LocalDate[] dateArray = new LocalDate[getDateBetween()];
        for (int i = 0; i < getDateBetween(); i++) {
            dateArray[i] = mFirstLocalDate.plusDays(i);
            System.out.println(dateArray[i]);
        }
        
        return dateArray;
    }
    
    public int getDateBetween() {
        return (int) ChronoUnit.DAYS.between(mFirstLocalDate, mLastLocalDate) + 1;
    }
    
    @Override
    public int compareTo(MainScheduleInfo o) {
        return Integer.compare(this.getmDDate(), o.getmDDate());
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(mFirstDate);
//        dest.writeString(mLastDate);
//        dest.writeValue(mFirstLocalDate);
//        dest.writeValue(mLastLocalDate);
//        dest.writeInt(mDDate);
//    }
}
