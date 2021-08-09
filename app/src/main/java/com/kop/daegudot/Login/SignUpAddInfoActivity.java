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
import com.kop.daegudot.Network.More.MyInfo.NicknameUpdate;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserRegister;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.Network.User.UserResponseStatus;
import com.kop.daegudot.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SignUpAddInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    EditText editName, editEmail;
    ImageButton backBtn;
    Button btnOk, btnCheckDup;

    Bundle bundle;

    SharedPreferences mTokenPref;
    String mNickname;
    String mEmail;

    boolean NICK_CHECKED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_add_info);

        Intent intent = getIntent();
        mNickname = intent.getStringExtra("nickname");
        mEmail = intent.getStringExtra("email");

        editName = findViewById(R.id.edit_nickName);
        editName.setText(mNickname);
        editEmail = findViewById(R.id.edit_email);
        editEmail.setText(mEmail);

        // TODO:
        //  db에서 회원 인지 확인

        backBtn = findViewById(R.id.backBtn);
        btnCheckDup = findViewById(R.id.btn_checkDup);
        btnOk = findViewById(R.id.btn_ok);

        backBtn.setOnClickListener(this);
        btnCheckDup.setOnClickListener(this);
        btnOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.btn_checkDup:
                mNickname = editName.getText().toString();
                selectNickname(mNickname);
                break;
            case R.id.btn_ok:
                if(NICK_CHECKED == true) selectEmail(mEmail);
                else Toast.makeText(getApplicationContext(), "별명 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void convertToMainActivity() {
        Intent intent = new Intent(SignUpAddInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void addUser(){
        UserRegister userRegister = new UserRegister();
        userRegister.nickname = mNickname;
        userRegister.email = mEmail;
        userRegister.password = null;
        userRegister.type = 'D';

        insertUser(userRegister);
    }

    //닉네임 중복 검사
    private void selectNickname(String nickname) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(null);
        Observable<UserResponseStatus> observable = service.checkNickDup(nickname);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponseStatus>() {
                    @Override
                    public void onNext(UserResponseStatus response) {
                        if(response.status == 1L){
                            Toast.makeText(getApplicationContext(), "중복된 별명입니다.", Toast.LENGTH_SHORT).show();
                            NICK_CHECKED = false;
                            Log.d("NICKNAME_DUP", "DUPLICATE NICKNAME" + " " + nickname);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "사용가능한 별명입니다.", Toast.LENGTH_SHORT).show();
                        NICK_CHECKED = true;
                        Log.d("NICKNAME_DUP", e.getMessage() + " " + nickname);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("NICKNAME_DUP", "complete");
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
                            Toast.makeText(getApplicationContext(), "중복된 이메일 입니다.", Toast.LENGTH_SHORT).show();
                            Log.d("EMAIl_DUP", "DUPLICATE EMAIL" + " " + email);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "사용가능한 이메일 입니다.", Toast.LENGTH_SHORT).show();
                        // Todo:
                        //  db에 회원가입 정보 저장하기 : email, pw, nickName
                        UserRegister userRegister = new UserRegister();
                        userRegister.email = mEmail;
                        userRegister.nickname = mNickname;
                        userRegister.password = "";
                        userRegister.type = 'D';

                        insertUser(userRegister);

                        Log.d("EMAIL_DUP", e.getMessage() + " " + email);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("EMAIL_DUP", "complete");
                    }
                })
        );
    }

    //회원가입
    private void insertUser(UserRegister userRegister) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(null);
        Observable<String> observable = service.registerUser(userRegister);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String response) {
                        Log.d("USER_EMAIL_REGISTER", "REGISTER Success!");
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        //SharedPreference에 토큰 저장하기
                        mTokenPref = getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mTokenPref.edit();
                        editor.putString("token", response);
                        editor.apply();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("USER_EMAIL_REGISTER", e.getMessage());
                        Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("USER_EMAIL_REGISTER", "complete");
                        //메인 화면으로 이동
                        convertToMainActivity();
                    }
                })
        );
    }
}