package com.kop.daegudot.MorePage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class MyInformationActivity extends AppCompatActivity {
    private Context mContext;
    private ArrayList<String> mArrayList;
    private ListView mListView;
    private MoreAdapter mMoreAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        this.mContext = getApplicationContext();

        prepareMenu();

        // TODO: 뒤로가기 버튼 구현 필요
        TextView title = findViewById(R.id.title);
        title.setText("내 정보 확인");

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
                    PasswordChangeDialog passwordChangeDialog = new PasswordChangeDialog(MyInformationActivity.this);
                    passwordChangeDialog.callFunctionForPassword();
                    break;
                case 1: //별명 변경 클릭
                    NicknameChangeDialog nicknameChangeDialog = new NicknameChangeDialog(MyInformationActivity.this);
                    nicknameChangeDialog.callFunctionForNickname();
                    break;
                case 2: //TODO: 로그아웃 기능 해주세요!
                    Toast.makeText(mContext, "로그아웃 시켜주세요ㅠㅠ",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
