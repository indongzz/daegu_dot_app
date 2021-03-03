package com.kop.daegudot.KakaoMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
        @SerializedName("address_name")
        @Expose
        private String addressName;
        @SerializedName("x")
        @Expose
        private double x;
        @SerializedName("y")
        @Expose
        private double y;
    
    
        public String getAddressName() {
            return addressName;
        }
    
        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }
        public double getX() {
            return x;
        }
    
        public void setX(double x) {
            this.x = x;
        }
    
        public double getY() {
            return y;
        }
    
        public void setY(double y) {
            this.y = y;
        }
    
    }
}
