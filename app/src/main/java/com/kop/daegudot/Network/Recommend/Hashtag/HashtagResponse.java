package com.kop.daegudot.Network.Recommend.Hashtag;

import android.os.Parcel;
import android.os.Parcelable;

import com.kop.daegudot.MySchedule.MainScheduleInfo;

import java.io.Serializable;

public class HashtagResponse implements Parcelable {
    public long id;
    public String content;
    
    public HashtagResponse() {
    
    }
    
    public HashtagResponse(Parcel in) {
        id = in.readLong();
        content = in.readString();
    }
    
    public static final Creator<HashtagResponse> CREATOR = new Creator<HashtagResponse>() {
        @Override
        public HashtagResponse createFromParcel(Parcel in) {
            return new HashtagResponse(in);
        }
        
        @Override
        public HashtagResponse[] newArray(int size) {
            return new HashtagResponse[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(content);
    }
}
