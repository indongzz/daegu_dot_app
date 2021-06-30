package com.kop.daegudot.Network.Map;

// public 으로 변수 변경
public class Place {
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
    
    @Override
    public boolean equals(Object object) {
        boolean equalName = false;
        
        if (object instanceof Place){
            equalName = this.attractName.equals(((Place) object).attractName);
        }
        
        return equalName;
    }
}
