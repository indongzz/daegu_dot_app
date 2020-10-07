package com.kop.daegudot.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kop.daegudot.MainActivity;
import com.kop.daegudot.R;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    EditText pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        ImageButton backBtn = findViewById(R.id.backBtn);

        email = findViewById(R.id.edit_email);
        pw = findViewById(R.id.edit_pw);

        System.out.println("email: " + email.getText());
        System.out.println("pw: " + pw.getText());

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
                if (checkEmail()) {
                    convertToMainActivity();
                }
                break;
        }
    }

    public boolean checkEmail() {
        String sEmail = email.getText().toString();
        String sPw = pw.getText().toString();

        // email 이 데이터베이스 정보와 일치 하면 shared pref
        // if (sEmail.equals()~~?)
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.clear();
        editor.apply();
        editor.putString("email", sEmail);
        editor.putString("pw", sPw);
        editor.apply();


        return true;
        // else이면 Toast 출력
        //Toast.makeText(getApplicationContext(), "아이디나 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT);
        // return false
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

}