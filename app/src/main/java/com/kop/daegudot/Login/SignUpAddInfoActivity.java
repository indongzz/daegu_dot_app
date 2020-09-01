package com.kop.daegudot.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kop.daegudot.MainActivity;
import com.kop.daegudot.R;

public class SignUpAddInfoActivity extends AppCompatActivity implements View.OnClickListener{

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

        int num = 1; // 1은 회원 2는 회원 아님
        if (num == 1) {
            // 회원 이면 ? 그냥 Main으로 넘어가기
            convertToMainActivity();
        } else {
            // 회원 아니면 이 activity가 뜨고 db에 데이터 추가
            editEmail.setText(email);
            editName.setText(name);
        }


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
                convertToMainActivity();
                break;
        }
    }

    public void convertToMainActivity() {
        Intent intent = new Intent(SignUpAddInfoActivity.this, MainActivity.class);
     //   intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}