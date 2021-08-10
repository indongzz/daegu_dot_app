package com.kop.daegudot.MorePage.MyWishlist.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="wishlist")
public class Wishlist {
    
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    @ColumnInfo(name="placeId")
    public long placeId;
    
    @ColumnInfo(name="attractName")
    public String attractName;
    
    @ColumnInfo(name="address")
    public String address;
    
    @ColumnInfo(name="star")
    public float star;
}

