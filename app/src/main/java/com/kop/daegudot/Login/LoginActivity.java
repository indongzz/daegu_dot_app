package com.kop.daegudot.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
//import com.kakao.auth.Session;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kop.daegudot.Login.KakaoLogin.KakaoLoginHandler;
//import com.kop.daegudot.Login.KakaoLogin.SessionCallback;
import com.kop.daegudot.MainActivity;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserOauth;
import com.kop.daegudot.Network.User.UserRegister;
import com.kop.daegudot.Network.User.UserResponseStatus;
import com.kop.daegudot.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // Google Login
    private static final int RC_SIGN_IN = 1001;
    private static final String TAG = "Oauth2Google";
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    // Kakao Login
//    private SessionCallback sessionCallback;
//    Session mSession;

//    SharedPreferences mPref;
//    public static SharedPreferences.Editor editor;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    // Register 객체
    private static UserRegister userRegister;

    private String mEmail;
    private String mNickname;
    SharedPreferences mTokenPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //   getHashKey();

        userRegister = new UserRegister();

        /* Google Sign In */
        findViewById(R.id.signin_google).setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        /* Kakao Sign In */
        ImageButton kakaoLoginBtn = findViewById(R.id.signin_kakao);
        kakaoLoginBtn.setOnClickListener(this);

        /* Email Sign In */
        Button emailLoginBtn = findViewById(R.id.signin_email);
        emailLoginBtn.setOnClickListener(this);

    }

    // SignIn Clicked
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Google Login Result
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /* 구글 로그인 결과 */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            UserOauth userOauth = new UserOauth();
            userOauth.oauthToken = account.getIdToken();
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getIdToken());
            oauthGoogle(userOauth);
        }
        catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(false);
        }
    }


    //google token id로부터 사용자 정보 추출하는 함수
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            mEmail = user.getEmail();
                            mNickname = user.getDisplayName();

                            Log.i(TAG, "firebaseAuthWithGoogle email: " + user.getEmail());
                            Log.i(TAG, "firebaseAuthWithGoogle name: " + user.getDisplayName());

                            //TODO: DB회원인지 아닌지 확인 아니면 UpdateUI함수 호출 맞으면 Main호출
                            selectEmail(mEmail);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(false);
                        }
                    }
                });
    }

    public void updateUI(boolean bool) {
        if (!bool) {
            Log.d(TAG, "updateUI: failed");
        } else {
            Log.d(TAG, "updateUI:success");
            Intent intent = new Intent(getApplicationContext(), SignUpAddInfoActivity.class);
            intent.putExtra("email", mEmail);
            intent.putExtra("nickname", mNickname);
            startActivity(intent);
            finish();
        }
    }

    public void convertToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signin_google) {
            googleSignIn();
        }
        if (v.getId() == R.id.signin_kakao) {
            KakaoLoginHandler kakaoLoginHandler = new KakaoLoginHandler(this);
            kakaoLoginHandler.kakaoLogin();
        }
        if (v.getId() == R.id.signin_email) {
            convertToEmailLogin();
        }
    }

    public void convertToEmailLogin() {
        Intent intent = new Intent(LoginActivity.this, EmailLoginActivity.class);
        startActivity(intent);
    }

    private String encryptSHA256(String pw) {
        StringBuilder result = new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pw.getBytes("UTF-8"));

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    result.append('0');
                }
                result.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    //구글 인증 처리
    private void oauthGoogle(UserOauth userOauth) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(null);
        Observable<Long> observable = service.oauthGoogle(userOauth);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.d("USER_GOOGLE", userOauth.oauthToken);
                        if(response == 1L) Toast.makeText(getApplicationContext(), "구글 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(getApplicationContext(), "구글 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("USER_GOOGLE", e.getMessage());
                        Toast.makeText(getApplicationContext(), "구글 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("USER_GOOGLE", "COMPLETE");
                        firebaseAuthWithGoogle(userOauth.oauthToken);
                    }
                })
        );
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
                            Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("EMAIl_DUP", "DUPLICATE EMAIL" + " " + response.userResponseDto.email);

                            //SharedPreference에 토큰 저장하기
                            mTokenPref = getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = mTokenPref.edit();
                            editor.putString("token", response.userResponseDto.token);
                            editor.apply();

                            convertToMainActivity();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "회원가입이 필요합니다.", Toast.LENGTH_SHORT).show();
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

    /* for registering Kakao developer
    private void getHashKey() {
        PackageInfo packageInfo = null;

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (packageInfo == null)
            Log.e("KeyHash", "LeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
    */
}