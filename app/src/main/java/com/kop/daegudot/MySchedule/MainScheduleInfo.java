package com.kop.daegudot.MySchedule;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MainScheduleInfo implements Comparable<MainScheduleInfo> , Parcelable{
    // 2020-02-02 format
    private LocalDate mFirstLocalDate;
    private LocalDate mLastLocalDate;
    private String mStartDate;
    private String mEndDate;
    private int mDDate;
    private long mainId;
    
    public MainScheduleInfo() {

    }

    protected MainScheduleInfo(Parcel in) {
        mStartDate = in.readString();
        mEndDate = in.readString();
        mFirstLocalDate = (LocalDate) in.readValue(LocalDate.class.getClassLoader());
        mLastLocalDate = (LocalDate) in.readValue(LocalDate.class.getClassLoader());
        mDDate = in.readInt();
        mainId = in.readLong();
    }

    public static final Creator<MainScheduleInfo> CREATOR = new Creator<MainScheduleInfo>() {
        @Override
        public MainScheduleInfo createFromParcel(Parcel in) {
            return new MainScheduleInfo(in);
        }

        @Override
        public MainScheduleInfo[] newArray(int size) {
            return new MainScheduleInfo[size];
        }
    };
    
    // ver.2
    public String getmStartDate() {
        return mStartDate;
    }
    
    public void setmStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }
    
    public String getmEndDate() {
        return mEndDate;
    }
    
    public void setmEndDate(String mEndDate) {
        this.mEndDate = mEndDate;
    }
    
    public int getmDDate() {
        return mDDate;
    }
    
    public void setmDDate() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yy.MM.dd");
        mFirstLocalDate = LocalDate.parse(mStartDate);
        mLastLocalDate = LocalDate.parse(mEndDate);
        LocalDate today = LocalDate.now();
        mDDate = (int) ChronoUnit.DAYS.between(today, mFirstLocalDate);
    }
    
    public long getMainId() {
        return mainId;
    }
    
    public void setMainId(long mainId) {
        this.mainId = mainId;
    }
    
    public String getButtonString() {
        // 년도 제외하고 월 일만 추출 + D-day
        // 리스트뷰에 들어가는 버튼 이름에 사용함.
        if (mDDate < 0) {
            int date = mDDate * (-1);
            return mStartDate.substring(5, 7) + "." + mStartDate.substring(8, 10) + " ~ " +
                    mEndDate.substring(5, 7) + "." + mEndDate.substring(8, 10) + " / " + "D+" + date;
        }
        return mStartDate.substring(5, 7) + "." + mStartDate.substring(8, 10)+ " ~ " +
                mEndDate.substring(5, 7) + "." + mEndDate.substring(8, 10) + " / " + "D-" + mDDate;
    }
    
    public String getDateString() {
        // 월 일만 추출
        // title로 사용
        return mStartDate.substring(5, 7) + "." + mStartDate.substring(8, 10)+ " ~ " +
                mEndDate.substring(5, 7) + "." +  mEndDate.substring(8, 10);
    }
    
    
    public LocalDate[] getDateArray() {
        LocalDate[] dateArray = new LocalDate[getDateBetween()];
        for (int i = 0; i < getDateBetween(); i++) {
            dateArray[i] = mFirstLocalDate.plusDays(i);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mStartDate);
        dest.writeString(mEndDate);
        dest.writeValue(mFirstLocalDate);
        dest.writeValue(mLastLocalDate);
        dest.writeInt(mDDate);
        dest.writeLong(mainId);
    }
}
