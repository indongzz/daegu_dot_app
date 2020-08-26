package com.kop.daegudot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        ImageButton backBtn = findViewById(R.id.backBtn);

        EditText email = findViewById(R.id.edit_email);
        EditText pw = findViewById(R.id.edit_pw);

        System.out.println("email: " + email.getText());
        System.out.println("pw: " + pw.getText());

        Button signUpBtn = findViewById(R.id.email_signup);

        backBtn.setOnClickListener(this);
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
        }
    }

    public void convertToSignUp() {
        Intent intent = new Intent(EmailLoginActivity.this, EmailSignUpActivity.class);
        startActivity(intent);
    }

}