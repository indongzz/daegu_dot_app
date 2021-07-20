package com.kop.daegudot.Network.Recommend;

import com.kop.daegudot.Network.Schedule.MainScheduleResponse;

public class RecommendResponse {
    long id;
    String title;
    String content;
    // List<Hashtag> hashtags;
    String localDateTime;
    double star;
    MainScheduleResponse mainScheduleResponse;
}
