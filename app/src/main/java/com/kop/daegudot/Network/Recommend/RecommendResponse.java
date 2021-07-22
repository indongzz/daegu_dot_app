package com.kop.daegudot.Network.Recommend;

import android.os.Parcel;
import android.os.Parcelable;

import com.kop.daegudot.Network.Recommend.Hashtag.HashtagResponse;
import com.kop.daegudot.Network.Schedule.MainScheduleResponse;

import java.util.ArrayList;

public class RecommendResponse implements Parcelable {
    public long id;
    public String title;
    public String content;
    public ArrayList<HashtagResponse> hashtags;
    public String localDateTime;
    public double star;
    public MainScheduleResponse mainScheduleResponseDto;
    
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
    }
}
