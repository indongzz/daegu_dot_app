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

import com.kop.daegudot.IntroPageActivity;
import com.kop.daegudot.MainActivity;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserLogin;
import com.kop.daegudot.Network.User.UserRegister;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.R;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
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

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String email;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_add_info);

        editName = findViewById(R.id.edit_nickName);
        editEmail = findViewById(R.id.edit_email);

        pref = getSharedPreferences("data", MODE_PRIVATE);
        email = pref.getString("email", null);
        nickname = pref.getString("name", null);

        backBtn = findViewById(R.id.backBtn);
        btnCheckDup = findViewById(R.id.btn_checkDup);
        btnOk = findViewById(R.id.btn_ok);

        backBtn.setOnClickListener(this);
        btnCheckDup.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        // TODO:
        //  db에서 회원 인지 확인
        DuplicateEmailRx(email);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.btn_checkDup:
                nickname = editName.getText().toString();
                checkNickName();
                break;
            case R.id.btn_ok:
                UserRegister userRegister = new UserRegister(email,"", nickname,'D');
                addUserRx(userRegister);
                convertToMainActivity();
                break;
        }
    }

    public void convertToMainActivity() {
        Intent intent = new Intent(SignUpAddInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void convertToLoginActivity(){
        Intent intent = new Intent(SignUpAddInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkNickName() {
        int len = nickname.length();

        if (len == 0) {
            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if (len < 2 || len > 6) {
            Toast.makeText(getApplicationContext(), "닉네임은 2글자 이상 6글자 이하로 설정해주세요", Toast.LENGTH_SHORT).show();
        } else {
            //닉네임 중복체크
            //중복X: NICK_CHECKED=true, 중복O: NICK_CHECKED=false;
            DuplicateNicknameRx(nickname);
        }
    }

    private void DuplicateNicknameRx(String nick) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<UserResponse> observable = service.checkNickDup(nick);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        Log.d("RX", response.toString());
                        editName.setText("");
                        Toast.makeText(getApplicationContext(), "사용 중인 닉네임 입니다", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX", e.getMessage());
                        Toast.makeText(getApplicationContext(), "사용 가능한 닉네임 입니다", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX", "complete");
                    }
                })
        );
    }

    private void DuplicateEmailRx(String email) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<UserResponse> observable = service.checkEmailDup(email);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        //회원 정보 존재
                        Log.d("RX", response.toString());
                        //Toast.makeText(getApplicationContext(), "사용 중인 이메일 입니다", Toast.LENGTH_SHORT).show();
                        convertToMainActivity();
                        testUserInfo(response.token);
                    }
                    @Override
                    public void onError(Throwable e) {
                        //새로 가입
                        Log.d("RX", e.getMessage());
                        editEmail.setText(email);
                        editName.setText(nickname);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX", "complete");
                    }
                })
        );
    }
    private void addUserRx(UserRegister userRegister) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<Long> observable = service.registerUser(userRegister);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long id) {
                        Log.d("RX", id+"가 추가되었습니다.");
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