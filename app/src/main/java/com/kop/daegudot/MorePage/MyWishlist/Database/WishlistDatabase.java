

package com.kop.daegudot.MorePage.MyWishlist.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Wishlist.class}, version = 1)
public abstract class WishlistDatabase extends RoomDatabase {
    
    private static volatile WishlistDatabase INSTANCE;
    
    public abstract WishlistDao wishlistDao();
    
    public static WishlistDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (WishlistDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WishlistDatabase.class, "Wishlists.db")
                            .build();
                }
            }
        }
        
        return INSTANCE;
    }
}

