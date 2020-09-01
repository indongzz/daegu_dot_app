package com.kop.daegudot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//
//        String name = bundle.getString("name") + " 님";

        Log.d("main activity", "main start --------");
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String name = pref.getString("name", null);

        Log.d("MainActivity", "name" + name);

        TextView tvName = findViewById(R.id.name);

        tvName.setText(name);
    }



}
