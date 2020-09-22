package com.kop.daegudot.Login.KakaoLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;
import com.kop.daegudot.Login.LoginActivity;

public class SessionCallback implements ISessionCallback {
    private Context mContext = null;

    public SessionCallback(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSessionOpened() {
        requestMe();
    }

    @Override
    public void onSessionOpenFailed(KakaoException e) {
        Log.e("SessionCallback :: ", "onSessionOpenFailed : " + e.getMessage());
    }

    public void requestMe() {
        UserManagement.getInstance()
                .me(new MeV2ResponseCallback() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        Log.i("KAKAO_API", "사용자 아이디: " + result.getId());

                        UserAccount kakaoAccount = result.getKakaoAccount();

                        if (kakaoAccount != null) {

                            String email = kakaoAccount.getEmail();

                            // get email
                            if (email != null) {
                                Log.i("KAKAO_API", "email: " + email);
                                LoginActivity.editor.putString("email", email);
                            } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                // get email after get agreement
                                Log.e("KAKAO_API", "need agreeemnt");
                            } else {
                                // cannot get email
                                Log.e("KAKAO_API", "cannot get email");
                            }

                            // get profile nickname
                            Profile profile = kakaoAccount.getProfile();
                            if (profile != null) {
                                Log.d("KAKAO_API", "name: " + profile.getNickname());
                                LoginActivity.editor.putString("name", profile.getNickname());
                            } else {
                                Log.e("KAKAO_API", "cannot get profile");
                            }

                            LoginActivity.editor.apply();
                            
                            redirectLoginActivity();
                        }
                    }
                });
    }

    private void redirectLoginActivity() {
        ((LoginActivity) mContext).updateUI(true);
    }

}
