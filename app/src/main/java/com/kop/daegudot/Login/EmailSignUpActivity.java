package com.kop.daegudot.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserRegister;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class EmailSignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    ImageButton backBtn;

    EditText editNickName, editEmail, editPw, editPwCheck;
    Button btnCheckDup, btnSignUp;

    String nickName, email, pw, pwCheck;

    String emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    boolean NICK_CHECKED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);

        backBtn = findViewById(R.id.backBtn);

        editNickName = findViewById(R.id.edit_SUName);
        btnCheckDup = findViewById(R.id.btn_checkDup);

        editEmail = findViewById(R.id.edit_SUemail);
        editPw = findViewById(R.id.edit_SUpw);
        editPwCheck = findViewById(R.id.edit_SUpwCheck);

        btnSignUp = findViewById(R.id.btn_SignUp);

        backBtn.setOnClickListener(this);
        btnCheckDup.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        editNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                NICK_CHECKED = false;
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_SignUp:
                if (checkInfo()) {
                    addData();
                }
                break;
            case R.id.backBtn:
                finish();
                break;
            case R.id.btn_checkDup:
                nickName = editNickName.getText().toString();
                checkNickName(nickName);
                break;
        }
    }

    public void addData() {
        // 자동 로그인을 위한 정보 저장
        /*SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        editor.putString("name", nickName);
        editor.putString("pw", pw);
        editor.apply();*/

        // Todo:
        //  db에 회원가입 정보 저장하기 : email, pw, nickName
        UserRegister userRegister = new UserRegister();
        userRegister.email = email;
        userRegister.nickname = nickName;
        userRegister.password = pw;
        userRegister.type = 'N';

        registerRx(userRegister);
        //LoginActivity.setRegisterInfo(email, pw, 'N');
    }

    public boolean checkInfo() {
        email = editEmail.getText().toString();
        pw = editPw.getText().toString();
        pwCheck = editPwCheck.getText().toString();

        if (!NICK_CHECKED) {
            editNickName.requestFocus();
            Toast.makeText(getApplicationContext(), "닉네임 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.matches(emailValidation) && email.length() > 0) {
            // 이메일 형식이 맞으면 넘어가기
            Log.d("CheckInfo", "email 형식 맞음" + email);
        } else {
            editEmail.requestFocus();
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pw.length() < 4 || pw.length() > 20) {
            editPw.requestFocus();
            Toast.makeText(getApplicationContext(), "비밀번호는 4 ~ 20자 사이로 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!pw.equals(pwCheck)) {
            editPwCheck.requestFocus();
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    public void checkNickName(String name) {
        int len = name.length();

        if (len == 0) {
            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (len < 2 || len > 6) {
            Toast.makeText(getApplicationContext(), "닉네임은 2글자 이상 6글자 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
        } else {
            // Todo: db 닉네임 중복 확인
            duplicateNicknameRx(name);
        }
    }


    public void convertToEmailLoginActivity() {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerRx(UserRegister userRegister) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(null);
        Observable<Long> observable = service.registerUser(userRegister);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("REGISTER", "REGISTER Success!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("REGISTER", e.getMessage());
                        //ToDo: 로그인 화면으로 전환할지 바로 메인으로 넘어갈지 정하기
                        convertToEmailLoginActivity();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("REGISTER", "complete");
                    }
                })
        );
    }
    private void duplicateNicknameRx(String nickname) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(null);
        Observable<UserResponse> observable = service.checkNickDup(nickname);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        Toast.makeText(getApplicationContext(), "중복된 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                        Log.d("NICKNAME", "DUPLICATE NICKNAME");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "사용가능한 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                        NICK_CHECKED = true;
                        Log.d("NICKNAME", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("NICKNAME", "complete");
                    }
                })
        );
    }
}