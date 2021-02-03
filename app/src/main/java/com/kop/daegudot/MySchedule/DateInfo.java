package com.kop.daegudot.MySchedule;

public class DateInfo {
    private String firstDate;
    private String lastDate;
    private String dDate;
    
    public String getFirstDate() {
        return firstDate;
    }
    
    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }
    
    public String getLastDate() {
        return lastDate;
    }
    
    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
    
    public String getdDate() {
        return dDate;
    }
    
    public void setdDate(String dDate) {
        this.dDate = dDate;
    }
    
    public String getTextString() {
        // 년도 제외하고 월 일만 추출
        return firstDate.substring(3, 8) + " ~ " + lastDate.substring(3, 8) + " / " + "D-" + dDate;
    }
    
}
