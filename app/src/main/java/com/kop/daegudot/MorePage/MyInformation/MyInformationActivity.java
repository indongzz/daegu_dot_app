package com.kop.daegudot.MorePage.MyInformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kop.daegudot.IntroPageActivity;
import com.kop.daegudot.MorePage.MoreAdapter;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.R;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Context mContext;
    private ArrayList<String> mArrayList;
    private ListView mListView;
    private MoreAdapter mMoreAdapter;
    
    private TextView mName;
    private TextView mEmail;

    private SharedPreferences pref;
    private String mToken;

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
        mToken = pref.getString("token", "");

        //ToDo: 토큰으로 사용자 정보 가져오기
        selectUserByToken();

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
                            new PasswordChangeDialog(MyInformationActivity.this, mToken);
                    passwordChangeDialog.callFunctionForPassword();
                    break;
                case 1: //별명 변경 클릭
                    NicknameChangeDialog nicknameChangeDialog =
                            new NicknameChangeDialog(MyInformationActivity.this, mToken, mName.getText().toString());
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
        
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
        
        // if type equals ("G") {
        //   FirebaseAuth.getInstance().signOut();

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
    
    public void updateNickNameUI(String name) {
        mName.setText(name);
    }

    //토큰으로 회원 정보 가져오기
    private void selectUserByToken() {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(mToken);
        Observable<UserResponse> observable = service.getUserFromToken();

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        mName.setText(response.nickname);
                        mEmail.setText(response.email);
                        Log.d("TOKEN", "TOKEN OK" + " " + response.email);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TOKEN", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TOKEN", "complete");
                    }
                })
        );
    }

}
