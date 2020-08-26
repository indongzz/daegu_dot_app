package com.kop.daegudot.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.kop.daegudot.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // Google Login
    private static final int RC_SIGN_IN = 1001;
    private static final String TAG = "Oauth2Google";
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    // Kakao Login
    private SessionCallback sessionCallback;
    Session mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //   getHashKey();

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

    // SignIn Clicked
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        Bundle bundle = new Bundle();
//        if(currentUser.getEmail() != null) {
//            bundle.putString("email", currentUser.getEmail());
//            bundle.putString("passwd", "google");
//            bundle.putString("name", currentUser.getDisplayName());
//        }
//        updateUI(bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("**********************************2");
        // Google Login Result
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }


    /* 구글 로그인 결과 */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
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
                            Bundle bundle = new Bundle();
                            bundle.putString("email", user.getEmail());
                            bundle.putString("passwd", "google");
                            bundle.putString("name", user.getDisplayName());
                            updateUI(bundle);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(Bundle account) {
        if (account == null) {
            Log.d(TAG, "updateUI:failure");
        } else {
            Log.d(TAG, "updateUI:success");
            Intent intent = new Intent(getApplicationContext(), SignUpAddInfoActivity.class);
            intent.putExtras(account);
            startActivity(intent);
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
        Intent intent = new Intent(LoginActivity.this, EmailLoginActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
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