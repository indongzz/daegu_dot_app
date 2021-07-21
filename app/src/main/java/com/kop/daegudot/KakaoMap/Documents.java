package com.kop.daegudot.KakaoMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kop.daegudot.Network.Map.Place;

import java.util.List;

public class Documents {
    @SerializedName("documents")
    @Expose
    private List<Address> documents = null;
    
    public List<Address> getDocuments() {
        return documents;
    }
    
    public void setDocuments(List<Address> documents) {
        this.documents = documents;
    }
    
    public class Address {
        @SerializedName("place_name")
        @Expose
        public String placeName;
        @SerializedName("road_address_name")
        @Expose
        public String address;
        @SerializedName("x")
        @Expose
        public double x;
        @SerializedName("y")
        @Expose
        public double y;
        @SerializedName("phone")
        @Expose
        public String phone;
    
        @Override
        public boolean equals(Object object) {
            boolean equalName = false;
        
            if (object instanceof Address){
                equalName = this.placeName.equals(((Address) object).placeName);
            }
        
            return equalName;
        }
    }
}
