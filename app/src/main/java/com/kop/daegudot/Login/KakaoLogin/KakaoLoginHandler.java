package com.kop.daegudot.Login.KakaoLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.ScopeInfo;
import com.kakao.sdk.user.model.User;
import com.kop.daegudot.Login.LoginActivity;
import com.kop.daegudot.Login.SignUpAddInfoActivity;
import com.kop.daegudot.MainActivity;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserOauth;
import com.kop.daegudot.Network.User.UserOauthResponse;
import com.kop.daegudot.Network.User.UserResponseStatus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import static android.content.Context.MODE_PRIVATE;

public class KakaoLoginHandler {
    // Google Login {
    private static final String TAG = "KakaoLoginHandler";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    
    Context mContext;
    String mEmail, mNickname;
    SharedPreferences mTokenPref;
    
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

            UserOauth userOauth  = new UserOauth();
            userOauth.oauthToken = oAuthToken.getAccessToken();
            oauthKakao(userOauth);
        }
        if (throwable != null) {
            // 로그인 실패
            Log.d("KakaoCallback", throwable.getLocalizedMessage());
            Toast.makeText(mContext, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
        return null;
    };
    
    private void requestMe() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (throwable != null) {
                Log.e(TAG, "사용자 정보 요청 실패" + throwable.getLocalizedMessage());
                Toast.makeText(mContext, "카카오 로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if (user != null) {
                /*Log.d(TAG, "User id: " + user.getId() +
                            "\nUser Email: " + user.getKakaoAccount().getEmail() +
                            "\nUser Nickname: " + user.getKakaoAccount().getProfile().getNickname());
                
                mEmail = user.getKakaoAccount().getEmail();
                mNickname = user.getKakaoAccount().getProfile().getNickname();*/

                selectEmail(mEmail);
            }
            return null;
        });
    }
    
    private void checkScopes() {
        List<String> scopes = new ArrayList<>();
        scopes.add("account_email");
        UserApiClient.getInstance().scopes(scopes, scopeCallback);
    }
    
    Function2<ScopeInfo, Throwable, Unit> scopeCallback = (scopeInfo, throwable) -> {
        if (scopeInfo != null) {
            Log.d(TAG, "scope: " + scopeInfo.getScopes());
            
            if (scopeInfo.getScopes().get(0).getAgreed()) {
                Log.d(TAG, "Scope agreed");
                updateUI(true);
            }
            else {
                Log.d(TAG, "Scope agreed false");
                incrementAuthRequest();
            }
        }
        if (throwable != null) {
            // 로그인 실패
            Log.d(TAG, "scope error: " + throwable.getLocalizedMessage());
            Toast.makeText(mContext, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
        }
        return null;
    };
    
    private void incrementAuthRequest() {
        List<String> scopes = new ArrayList<>();
        scopes.add("account_email");
        UserApiClient.getInstance().loginWithNewScopes(mContext, scopes, kakaoCallback);
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
    //카카오 인증 처리
    private void oauthKakao(UserOauth userOauth) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(null);
        Observable<UserOauthResponse> observable = service.oauthKakao(userOauth);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserOauthResponse>() {
                    @Override
                    public void onNext(UserOauthResponse response) {
                        Log.d("USER_KAKAO", userOauth.oauthToken);
                        if(response.status == 1L){
                            Toast.makeText(mContext, "카카오 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            mEmail = response.email;
                            mNickname = response.nickname;
                        }
                        else Toast.makeText(mContext, "카카오 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("USER_KAKAO", e.getMessage());
                        Toast.makeText(mContext, "카카오 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("USER_GOOGLE", "COMPLETE");
                        requestMe();
                    }
                })
        );
    }

    private void convertToMainActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        ((LoginActivity) mContext).finish();
    }

    //이메일 중복 검사
    private void selectEmail(String email) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(null);
        Observable<UserResponseStatus> observable = service.checkEmailDup(email);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponseStatus>() {
                    @Override
                    public void onNext(UserResponseStatus response) {
                        if(response.status == 1L){
                            Toast.makeText(mContext, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("EMAIl_DUP", "DUPLICATE EMAIL" + " " + response.userResponseDto.email);

                            //SharedPreference에 토큰 저장하기
                            mTokenPref = mContext.getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = mTokenPref.edit();
                            editor.putString("token", response.userResponseDto.token);
                            editor.apply();

                            convertToMainActivity();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, "회원가입이 필요합니다.", Toast.LENGTH_SHORT).show();
                        Log.d("EMAIL_DUP", e.getMessage() + " " + email);
                        updateUI(true);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("EMAIL_DUP", "complete");
                    }
                })
        );
    }
}
