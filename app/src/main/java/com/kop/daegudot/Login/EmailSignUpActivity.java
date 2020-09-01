package com.kop.daegudot.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kop.daegudot.MainActivity;
import com.kop.daegudot.R;

import org.w3c.dom.Text;

public class EmailSignUpActivity extends AppCompatActivity implements View.OnClickListener{
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
                if (NICK_CHECKED) {
                    if (!nickName.equals(editNickName.getText().toString())) {
                        NICK_CHECKED = false;
                        updateUI(false);
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_SignUp:
                // db에 저장하고 main으로 넘어가기
                // add to db
                if (checkInfo()) {
                    convertToMainActivity();
                }
                break;
            case R.id.backBtn:
                finish();
                break;
            case R.id.btn_checkDup:
                nickName = editNickName.getText().toString();
                // 있는지 확인 후 ui 업데이트
                checkNickName(nickName);
                break;
        }
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
            updateUI(true);

            NICK_CHECKED = true;
        }
    }

    public void updateUI(boolean bool) {
        if (bool) { // 닉네임 중복 확인 완료 후 버튼 비활성화
//            btnCheckDup.setBackground(getDrawable(R.drawable.round_btn));
            btnCheckDup.setBackgroundTintList(ColorStateList.valueOf(
                    getResources().getColor(R.color.lightGray, getTheme())));
            btnCheckDup.setClickable(false);
            Toast toast = Toast.makeText(getApplicationContext(), "사용 가능한 닉네임 입니다", Toast.LENGTH_LONG);
            toast.show();
        } else { // 닉네임 수정하면 버튼 활성화
            btnCheckDup.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            btnCheckDup.setBackground(getDrawable(R.drawable.round_btn));
            btnCheckDup.setClickable(true);

        }
    }

    public void convertToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}