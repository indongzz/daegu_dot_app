package com.kop.daegudot.Login.KakaoLogin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.kop.daegudot.Login.LoginActivity;
import com.kop.daegudot.Login.SignUpAddInfoActivity;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class KakaoLoginHandler {
    private static final String TAG = "KakaoLoginHandler";
    
    Context mContext;
    String mEmail, mNickname;
    
    public KakaoLoginHandler(Context context) {
        mContext = context;
    }
    
    public void kakaoLogin() {
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(mContext)) {
            UserApiClient.getInstance().loginWithKakaoTalk(mContext, kakaoCallback);
        } else {
            UserApiClient.getInstance().loginWithKakaoAccount(mContext, kakaoCallback);
        }
    }
    
    Function2<OAuthToken, Throwable, Unit> kakaoCallback = (oAuthToken, throwable) -> {
        if (oAuthToken != null) {
            Log.d(TAG, "로그인 성공 token: " + oAuthToken.toString());
            // token:
            //      oAuthToken.getAccessToken();
            // TODO: 로그인 후 처리
        }
        if (throwable != null) {
            // 로그인 실패
            Log.d("KakaoCallback", throwable.getLocalizedMessage());
            Toast.makeText(mContext, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }
        // 사용자 정보 받아옴
        requestMe();
        return null;
    };
    
    private void requestMe() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (throwable != null) {
                Log.e(TAG, "사용자 정보 요청 실패" + throwable.getLocalizedMessage());
            }
            else if (user != null) {
                Log.d(TAG, "User id: " + user.getId() +
                            "\nUser Email: " + user.getKakaoAccount().getEmail() +
                            "\nUser Nickname: " + user.getKakaoAccount().getProfile().getNickname());
                
                mEmail = user.getKakaoAccount().getEmail();
                mNickname = user.getKakaoAccount().getProfile().getNickname();
                updateUI(true);
            }
            return null;
        });
    }
    
    public void updateUI(boolean bool) {
        if (!bool) {
            Log.d(TAG, "updateUI: failed");
        } else {
            Log.d(TAG, "updateUI:success");
            Intent intent = new Intent(mContext, SignUpAddInfoActivity.class);
            intent.putExtra("email", mEmail);
            intent.putExtra("nickname", mNickname);
            mContext.startActivity(intent);
            ((LoginActivity) mContext).finish();
        }
    }
}
