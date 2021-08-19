package com.kop.daegudot.Login.KakaoLogin;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.kakao.sdk.common.KakaoSdk;
import com.kop.daegudot.MorePage.MyWishlist.Database.WishlistDatabase;
import com.kop.daegudot.R;

// initiate Kakao SDK
public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    public static WishlistDatabase db = null;

    public static GlobalApplication getGlobalApplicationContext() {
        if (instance == null) {
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        KakaoSdk.init(this, getResources().getString(R.string.kakao_native_app_key_wo));

        db = WishlistDatabase.getInstance(getGlobalApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
