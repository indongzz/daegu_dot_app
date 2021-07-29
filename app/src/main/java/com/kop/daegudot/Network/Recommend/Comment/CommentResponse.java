package com.kop.daegudot.Network.Recommend.Comment;

import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.User.UserResponse;

public class CommentResponse {
    public long id;
    public String dateTime;
    public String comments;
    public UserResponse userResponseDto;
    public RecommendResponse recommendScheduleResponseDto;
}
