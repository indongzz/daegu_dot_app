package com.kop.daegudot.MySchedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateInfo implements Comparable<DateInfo> {
    private String mFirstDate;
    private String mLastDate;
    private int mDDate;
    private LocalDate mFirstLocalDate;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yy.MM.dd");
    
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
        mFirstLocalDate = LocalDate.parse(mFirstDate, format);
        LocalDate today = LocalDate.now();
        mDDate = (int) ChronoUnit.DAYS.between(today, mFirstLocalDate);
    }
    
    public String getTextString() {
        // 년도 제외하고 월 일만 추출
        return mFirstDate.substring(3) + " ~ " + mLastDate.substring(3) + " / " + "D-" + mDDate;
    }
    
    
    public LocalDate[] getDateArray() {
        LocalDate[] dateArray = new LocalDate[mDDate];
        for (int i = 0; i < mDDate; i++) {
            dateArray[i] = mFirstLocalDate.plusDays(i);
            System.out.println(dateArray[i]);
        }
        
        return dateArray;
    }
    
    @Override
    public int compareTo(DateInfo o) {
        return Integer.compare(this.getmDDate(), o.getmDDate());
    }
}
