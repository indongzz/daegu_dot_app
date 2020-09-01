package com.kop.daegudot.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kop.daegudot.R;

import org.w3c.dom.Text;

public class EmailSignUpActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton backBtn;

    EditText editNickName, editEmail, editPw, editPwCheck;
    Button btnCheckDup, btnSignUp;

    String nickName, email, pw;

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

        pwCheck();

        nickName = editNickName.getText().toString();
        email = editEmail.getText().toString();
        pw = editPw.getText().toString();

        backBtn.setOnClickListener(this);
        btnCheckDup.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }


    public void pwCheck() {
        editPwCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editPw.getText().toString().equals(editPwCheck.getText().toString())) {
                    editPw.setHighlightColor(Color.GREEN);
                    editPwCheck.setHighlightColor(Color.GREEN);
                } else {
                    editPw.setHighlightColor(Color.RED);
                    editPwCheck.setHighlightColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_SignUp:
                break;
            case R.id.backBtn:
                finish();
                break;
            case R.id.btn_checkDup:
                break;
        }
    }
}