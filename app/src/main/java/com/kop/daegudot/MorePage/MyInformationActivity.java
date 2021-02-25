package com.kop.daegudot.MorePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kop.daegudot.IntroPageActivity;
import com.kop.daegudot.MainActivity;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.Objects;

public class MyInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private ArrayList<String> mArrayList;
    private ListView mListView;
    private MoreAdapter mMoreAdapter;
    
    private TextView mName;
    private TextView mEmail;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        this.mContext = getApplicationContext();

        prepareMenu();
    
        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        TextView title = findViewById(R.id.title);
        title.setText("내 정보 확인");
        
        mName = findViewById(R.id.profile_name);
        mEmail = findViewById(R.id.profile_email);
        
        pref = getSharedPreferences("data", MODE_PRIVATE);
        if (!Objects.equals(pref.getString("email", ""), "")) {
            mEmail.setText(pref.getString("email", ""));
        }

        mListView = findViewById(R.id.myinfo_list);
        mListView.setOnItemClickListener(onItemClickListener);
        mMoreAdapter = new MoreAdapter(mContext, mArrayList);
        mListView.setAdapter(mMoreAdapter);
    }

    private void prepareMenu(){
        mArrayList = new ArrayList<>();
        mArrayList.add("비밀번호 변경");
        mArrayList.add("닉네임 변경");
        mArrayList.add("로그아웃");
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switch (position){
                case 0: //비밀번호 변경 클릭
                    PasswordChangeDialog passwordChangeDialog =
                            new PasswordChangeDialog(MyInformationActivity.this);
                    passwordChangeDialog.callFunctionForPassword();
                    break;
                case 1: //별명 변경 클릭
                    NicknameChangeDialog nicknameChangeDialog =
                            new NicknameChangeDialog(MyInformationActivity.this);
                    nicknameChangeDialog.callFunctionForNickname();
                    break;
                case 2:
                    logout();
                    break;
                default:
                    break;
            }
        }
    };
    
    private void logout() {
        // TODO: 로그아웃!!
        String passwd = pref.getString("passwd", "");
        if (passwd.equals("google")) {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
        } else if (passwd.equals("kakao")) {
            Toast.makeText(mContext, "카카오", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "이메일", Toast.LENGTH_SHORT).show();
        }

        ActivityCompat.finishAffinity(this);
        Intent intent = new Intent(this, IntroPageActivity.class);
        startActivity(intent);
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                finish();
                break;
        }
        
    }
}
