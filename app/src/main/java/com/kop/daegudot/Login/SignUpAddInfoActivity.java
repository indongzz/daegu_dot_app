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
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserResponse;
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

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_add_info);

        editName = findViewById(R.id.edit_nickName);
        editEmail = findViewById(R.id.edit_email);

        pref = getSharedPreferences("data", MODE_PRIVATE);
        String email = pref.getString("email", null);
        String name = pref.getString("name", null);

        /// Google & Kakao login 정보를 받고, 회원인지 아닌지 파악 (db에서 찾)

        // TODO:
        //  db에서 회원 인지 확인
//        if ( 회원 인 경우)
            // 회원 이면 ? 그냥 Main으로 넘어가기
            convertToMainActivity();
//        } else { 회원 아닌 경우
            // 회원 아니면 이 activity 띄우기
            editEmail.setText(email);
            editName.setText(name);
//        }


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
                // 닉네임 중복 확인
                break;
            case R.id.btn_ok:
                // 회원가입 모두 완료
                // TODO:
                //  db에 데이터 추가
                convertToMainActivity();
                break;
        }
    }

    public void convertToMainActivity() {
        Intent intent = new Intent(SignUpAddInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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