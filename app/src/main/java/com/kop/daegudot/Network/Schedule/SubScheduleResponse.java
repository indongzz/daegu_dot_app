package com.kop.daegudot.Network.Schedule;

import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.Network.Map.Place;

import java.time.LocalDate;

public class SubScheduleResponse implements Comparable<SubScheduleResponse> {
    public long id;
    public String date;
    public Place placesResponseDto;
    
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
}
