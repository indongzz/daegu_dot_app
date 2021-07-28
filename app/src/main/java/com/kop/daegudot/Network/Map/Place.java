package com.kop.daegudot.Network.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
    public long id; // db 고유 번호
    public int tag; // 마커 tag 번호
    public String address;
    public String attractContents;
    public String attractName;
    public String homepage;
    public String telephone;
    public float rate;
    public boolean like;
    public String longitude;
    public String latitude;
    public String category;
    
    public Place() {
    
    }
    
    protected Place(Parcel in) {
        id = in.readLong();
        tag = in.readInt();
        address = in.readString();
        attractContents = in.readString();
        attractName = in.readString();
        homepage = in.readString();
        telephone = in.readString();
        rate = in.readFloat();
        like = in.readByte() != 0;
        longitude = in.readString();
        latitude = in.readString();
        category = in.readString();
    }
    
    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }
        
        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
    
    @Override
    public boolean equals(Object object) {
        boolean equalName = false;
        
        if (object instanceof Place){
            equalName = this.attractName.equals(((Place) object).attractName);
        }
        
        return equalName;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(tag);
        dest.writeString(address);
        dest.writeString(attractContents);
        dest.writeString(attractName);
        dest.writeString(homepage);
        dest.writeString(telephone);
        dest.writeFloat(rate);
        dest.writeByte((byte) (like ? 1 : 0));
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(category);
    }
}
