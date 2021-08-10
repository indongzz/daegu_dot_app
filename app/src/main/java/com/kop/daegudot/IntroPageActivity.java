package com.kop.daegudot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.kop.daegudot.Login.LoginActivity;

import io.reactivex.disposables.CompositeDisposable;

public class IntroPageActivity extends AppCompatActivity {
    SharedPreferences mPref;
    private String mToken;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mPref = getSharedPreferences("data", MODE_PRIVATE);
                mToken = mPref.getString("token", "");

                if(mToken == "")
                    mIntent = new Intent(IntroPageActivity.this, LoginActivity.class);
                else
                    mIntent = new Intent(IntroPageActivity.this, MainActivity.class);

                startActivity(mIntent);
                finish();
            }
        }, 3000);
    }


}