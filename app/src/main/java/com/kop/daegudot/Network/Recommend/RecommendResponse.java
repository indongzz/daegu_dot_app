package com.kop.daegudot.Network.Recommend;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.kop.daegudot.Network.Recommend.Comment.CommentResponse;
import com.kop.daegudot.Network.Recommend.Hashtag.HashtagResponse;
import com.kop.daegudot.Network.Schedule.MainScheduleResponse;
import com.kop.daegudot.Network.User.UserResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecommendResponse implements Parcelable, Comparable<RecommendResponse> {
    public long id;
    public String title;
    public String content;
    public ArrayList<HashtagResponse> hashtags;
    public String localDateTime;
    public double star;
    public MainScheduleResponse mainScheduleResponseDto;
    public UserResponse userResponseDto;
    
    public RecommendResponse() {
    
    }
    
    protected RecommendResponse(Parcel in) {
        id = in.readLong();
        title = in.readString();
        content = in.readString();
        hashtags = in.readArrayList(HashtagResponse.class.getClassLoader());
        localDateTime = in.readString();
        star = in.readDouble();
        mainScheduleResponseDto = in.readParcelable(MainScheduleResponse.class.getClassLoader());
        userResponseDto = in.readParcelable(UserResponse.class.getClassLoader());
    }
    
    public float getStar() {
        return (float)star;
    }
    
    public static final Creator<RecommendResponse> CREATOR = new Creator<RecommendResponse>() {
        @Override
        public RecommendResponse createFromParcel(Parcel in) {
            return new RecommendResponse(in);
        }
        
        @Override
        public RecommendResponse[] newArray(int size) {
            return new RecommendResponse[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeList(hashtags);
        dest.writeString(localDateTime);
        dest.writeDouble(star);
        dest.writeParcelable(mainScheduleResponseDto, 0);
        dest.writeParcelable(userResponseDto, 0);
    }
    
    @Override
    public boolean equals(@Nullable Object obj) {
        boolean bool = false;
        
        if (obj instanceof RecommendResponse) {
            bool = this.id == ((RecommendResponse) obj).id;
        }
        
        return bool;
    }
    
    
    @Override
    public int compareTo(RecommendResponse o) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime date1 = LocalDateTime.parse(localDateTime, formatter);
        LocalDateTime date2 = LocalDateTime.parse(o.localDateTime, formatter);
    
        if (date1.isBefore(date2)) {
            return -1;
        } else if (date1.isAfter(date2)) {
            return 1;
        } else {
            return 0;
        }
    }
}
