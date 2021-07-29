package com.kop.daegudot.Network.Recommend.Comment;

import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.User.UserResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentResponse {
    public long id;
    public String dateTime;
    public String comments;
    public UserResponse userResponseDto;
    public RecommendResponse recommendScheduleResponseDto;
    
    public String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime date = LocalDateTime.parse(dateTime, formatter);
        
        String time = date.format(DateTimeFormatter.ofPattern("M/d hh:mm"));
        
        return time;
    }
}
