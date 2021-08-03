package com.kop.daegudot.MorePage.MyWishlist.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface WishlistDao {
    @Insert
    public Completable insertWishlist(Wishlist wishlist);
    
    @Delete
    public Completable deleteWishlist(Wishlist wishlist);
    
    @Query("select * from wishlist")
    public Flowable<List<Wishlist>> selectAllWishlists();
}
