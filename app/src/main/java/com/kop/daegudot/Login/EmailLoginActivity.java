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
import android.widget.Toast;

import com.kop.daegudot.MainActivity;
import com.kop.daegudot.R;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserLogin;
import com.kop.daegudot.Network.User.UserResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    EditText pw;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

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
                UserLogin userLogin = new UserLogin(email.getText().toString(), pw.getText().toString());
                startRx(userLogin);
                break;
        }
    }

    public void convertToSignUp() {
        Intent intent = new Intent(EmailLoginActivity.this, EmailSignUpActivity.class);
        startActivity(intent);
    }

    public void convertToMainActivity(UserResponse userResponse) {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
        editor.putString("email", userResponse.email);
        editor.putString("pw", userResponse.password);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startRx(UserLogin userLogin) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<UserResponse> observable = service.requestLogin(userLogin);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        Log.d("RX", response.toString());
                        convertToMainActivity(response);
                        testUserInfo(response.token); //토큰 테스트
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX", e.getMessage());
                        Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX", "complete");
                    }
                })
        );
    }

    private void testUserInfo(String token){
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(token);
        Observable<UserResponse> observable = service.getUserInfo();

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {
                        Log.d("RX", userResponse.toString());
                        Toast.makeText(getApplicationContext(), "사용자 닉네임 : " + userResponse.nickname, Toast.LENGTH_LONG).show();
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