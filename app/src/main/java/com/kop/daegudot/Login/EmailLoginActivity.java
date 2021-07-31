package com.kop.daegudot.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kop.daegudot.MainActivity;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserLogin;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    EditText email;
    EditText pw;

    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        ImageButton backBtn = findViewById(R.id.backBtn);

        email = findViewById(R.id.edit_email);
        pw = findViewById(R.id.edit_pw);

        Button loginBtn = findViewById(R.id.btn_login);
        Button signUpBtn = findViewById(R.id.email_signup);

        backBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.email_signup:
                convertToSignUp();
                break;
            case R.id.backBtn:
                finish();
                break;
            case R.id.btn_login:
                checkEmail();
                break;
        }
    }

    public void checkEmail(){
        UserLogin userLogin = new UserLogin();
        userLogin.email = email.getText().toString();
        userLogin.password = pw.getText().toString();

        selectEmailAndPassword(userLogin);
    }

    public void convertToSignUp() {
        Intent intent = new Intent(EmailLoginActivity.this, EmailSignUpActivity.class);
        startActivity(intent);
    }

    public void convertToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    //로그인 함수
    private void selectEmailAndPassword(UserLogin userLogin) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(null);
        Observable<UserResponse> observable = service.requestLogin(userLogin);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        Log.d("USER_EMAIL_LOGIN", "LOGIN SUCCESS");

                        //SharedPreference에 토큰 저장하기
                        mPref = getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPref.edit();
                        editor.putString("token", response.token);
                        editor.putString("type", "N");
                        editor.apply();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("USER_EMAIL_LOGIN", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("USER_EMAIL_LOGIN", "complete");
                        //메인 화면으로 이동
                        convertToMainActivity();
                    }
                })
        );
    }
}