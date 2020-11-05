package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import net.daum.mf.map.api.MapPoint;

import java.io.IOException;
import java.util.List;

public class MarkerInfo {
    private String name;
    private String address;
    private MapPoint addressPoint;
    private String summary;
    private String tel;
    private float rate;
    private boolean like;
    Context mContext;
    
    public MarkerInfo(Context context) {
        mContext = context;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
        setAddressPoint(changeToMapPoint(address));
    }
    
    public MapPoint getAddressPoint() {
        return addressPoint;
    }
    
    public void setAddressPoint(MapPoint addressPoint) {
        this.addressPoint = addressPoint;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getTel() {
        return tel;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    public float getRate() {
        return rate;
    }
    
    public void setRate(float rate) {
        this.rate = rate;
    }
    
    
    public MapPoint changeToMapPoint(String address) {
        Geocoder geocoder = new Geocoder(mContext);
        
        List<Address> list = null;
        MapPoint mapPoint = null;
    
        try {
            list = geocoder.getFromLocationName(address, 20);
         //   System.out.println(list);
            mapPoint = MapPoint.mapPointWithGeoCoord(list.get(0).getLatitude(), list.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("GetLocationName", "location error");
        }
        
        return mapPoint;
    }
    
    public void setLike(boolean bool) {
        like = bool;
    }
    
    public boolean isLiked() {
        return like;
    }
}
