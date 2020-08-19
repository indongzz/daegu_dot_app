package com.kop.daegudot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.auth.AuthType;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class TestLoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);

        Intent intent = getIntent();
        if (intent != null) {
            String userId = intent.getExtras().getString("kakaoUser");
            if (userId == null) {
                userId = intent.getExtras().getString("googleUser");
            }
            TextView tv = findViewById(R.id.tv_user);
            tv.setText(userId);
        }

        Button googleSignOut = findViewById(R.id.signout_google);
        googleSignOut.setOnClickListener(this);

        Button kakaoSignOut = findViewById(R.id.signout_kakao);
        kakaoSignOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.signout_google:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.signout_kakao:
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
        }
    }
}