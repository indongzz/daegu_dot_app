package com.kop.daegudot.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.kop.daegudot.Login.KakaoLogin.SessionCallback;
import com.kop.daegudot.MainActivity;
import com.kop.daegudot.R;
import com.kop.daegudot.network.RestApiService;
import com.kop.daegudot.network.RestfulAdapter;
import com.kop.daegudot.network.UserRequest;
import com.kop.daegudot.network.UserResponse;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // Google Login
    private static final int RC_SIGN_IN = 1001;
    private static final String TAG = "Oauth2Google";
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    // Kakao Login
    private SessionCallback sessionCallback;
    Session mSession;

    SharedPreferences pref ;
    public static SharedPreferences.Editor editor;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //   getHashKey();

        pref = getSharedPreferences("data", MODE_PRIVATE);
        editor = pref.edit();

        checkAlreadyLogin();

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

        sessionCallback = new SessionCallback(this);

        LoginButton kakaoLoginBtn = findViewById(R.id.signin_kakao);
        mSession = Session.getCurrentSession();
        mSession.addCallback(sessionCallback);

        kakaoLoginBtn.setOnClickListener(this);

        /* Email Sign In */
        Button emailLoginBtn = findViewById(R.id.signin_email);
        emailLoginBtn.setOnClickListener(this);

    }

    public void checkAlreadyLogin() {
        pref = getSharedPreferences("data", MODE_PRIVATE);
        Log.d("checkAlreadyLogin", "...Check login...");
        if (pref.getString("email", "") != "") {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            Log.d("checkAlreadyLogin", "Already Logged in");
        }
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

        // Kakao Login result
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /* 구글 로그인 결과 */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
        }
        catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(false);
        }
    }


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
                            editor.putString("email", user.getEmail());
                            editor.putString("passwd", "google");
                            editor.putString("name", user.getDisplayName());
                            editor.commit();

                            updateUI(true);
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
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signin_google:
                googleSignIn();
                break;
            case R.id.signin_kakao:
                mSession.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
                break;
            case R.id.signin_email:
                convertToEmailLogin();
                break;
        }
    }

    public void convertToEmailLogin() {

        String email = "test";
        String nickname = "test123";
        String pw = "test1234";
        char type = 'a';
        //String encryptedPassWord = encryptSHA256(pw);

        UserRequest userRequest = new UserRequest(email, nickname, /*encryptedPassWord*/ pw, type);
        startRx(userRequest);

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
        Session.getCurrentSession().removeCallback(sessionCallback);

        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
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

    private void startRx(UserRequest userRequest) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<UserResponse> observable = service.requestLogin(userRequest);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        Log.d("RX", response.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX", "complete");
                    }
                })
        );
    }
}