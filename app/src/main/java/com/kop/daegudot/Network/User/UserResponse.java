package com.kop.daegudot.Network.User;

import android.os.Parcel;
import android.os.Parcelable;

public class UserResponse implements Parcelable {
    public Long id;
    public String email;
    public String nickname;
    public String password;
    public String token;
    public char type;
    
    protected UserResponse(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        email = in.readString();
        nickname = in.readString();
        password = in.readString();
        token = in.readString();
        type = (char) in.readInt();
    }
    
    public static final Creator<UserResponse> CREATOR = new Creator<UserResponse>() {
        @Override
        public UserResponse createFromParcel(Parcel in) {
            return new UserResponse(in);
        }
        
        @Override
        public UserResponse[] newArray(int size) {
            return new UserResponse[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(email);
        dest.writeString(nickname);
        dest.writeString(password);
        dest.writeString(token);
        dest.writeInt((int) type);
    }
}
