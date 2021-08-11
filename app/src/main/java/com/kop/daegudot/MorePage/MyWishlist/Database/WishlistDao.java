package com.kop.daegudot.MorePage.MyWishlist.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

@Dao
public interface WishlistDao {
    @Insert
    public Completable insertWishlist(Wishlist wishlist);
    
    @Delete
    public Completable deleteWishlist(Wishlist wishlist);
    
    @Query("select * from wishlist")
    public Flowable<List<Wishlist>> selectAllWishlists();
    
    @Query("SELECT * from wishlist WHERE placeId = :placeId")
    public Observable<Wishlist> selectWishlistByPlaceId(long placeId);
}
