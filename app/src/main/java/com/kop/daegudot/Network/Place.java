package com.kop.daegudot.Network;

import net.daum.mf.map.api.MapPoint;

public class Place {
    private String address;
    private String attractContents;
    private String attractName;
    private String homepage;
    private String telephone;
    private float rate;
    private boolean like;
    private MapPoint mapPoint;
    
    public String getAddress() {
        return address;
    }
    
    public String getAttractContents() {
        return attractContents;
    }
    
    public String getAttractName() {
        return attractName;
    }
    
    public String getHomepage() {
        return homepage;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public float getRate() {
        return rate;
    }
    
    public void setRate(float rate) {
        this.rate = rate;
    }
    
    public boolean isLiked() {
        return like;
    }
    
    public void setLike(boolean like) {
        this.like = like;
    }
    
    public MapPoint getMapPoint() {
        return mapPoint;
    }
    
    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
    }
}
