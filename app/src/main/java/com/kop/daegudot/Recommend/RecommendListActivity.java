package com.kop.daegudot.Recommend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kop.daegudot.Network.Recommend.Hashtag.HashtagResponse;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.Recommend.RecommendResponseList;
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

/**
 * 추천글 목록을 띄우는 Activity
 * 해시태그 클릭 시 뜸
 */
public class RecommendListActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "RecommendListActivity";
    public static final int REQUEST_CODE = 100;
    
    Context mContext;
    ImageButton backBtn;
    ImageButton refreshBtn;
    RecyclerView mRecyclerView;
    static PostAdapter mAdapter;
    
    View mView;
    HashtagResponse mHashtag;
    ProgressBar progressBar;
    DrawerViewControl mDrawerViewControl;
    
    /* RX java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mToken;
    ArrayList<RecommendResponse> mRecommendList = new ArrayList<>();
    UserResponse userResponse;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mView = getLayoutInflater().inflate(R.layout.activity_recommand_list, null);
        setContentView(mView);
        
        mContext = this;
    
        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        selectUserByToken();
        
        Intent intent = getIntent();
        
        if (intent.getParcelableExtra("hashtag") != null) {
            mHashtag = intent.getParcelableExtra("hashtag");
            TextView title = findViewById(R.id.title);
            title.setText(mHashtag.content);
        }
        
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(this);
        
        progressBar = findViewById(R.id.progress_bar);
        
        mRecyclerView = findViewById(R.id.recommend_list_view);
        
        selectAllRecommendListRx(mHashtag);
        
        mAdapter = new PostAdapter(mContext, mRecommendList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setReverseLayout(true); //역순 출력
        llm.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(llm);
        
        
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        
        ImageButton backbtn2 = findViewById(R.id.drawer_backBtn);
        backbtn2.setOnClickListener(this);
        
    }
    
    public void selectAllRecommendListRx(HashtagResponse hashtag) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<RecommendResponseList> observable = service.selectAllRecommendList(hashtag.id);
    
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RecommendResponseList>() {
                    @Override
                    public void onNext(RecommendResponseList response) {
                        Log.d("RX " + TAG, "Next");
                        if (response.status == 1L) {
                            mRecommendList.clear();
                            mRecommendList.addAll(response.recommendScheduleResponseDtoArrayList);
                        } else if (response.status == 0L) {
                            Toast.makeText(mContext, "cannot get recommendList", Toast.LENGTH_SHORT).show();
                        }
                    }
                
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");
                        
                        updateUI();
                        progressBar.setVisibility(View.GONE);
                    }
                })
        );
    }
    
    public void updateUI() {
        mRecyclerView.setAdapter(mAdapter);
        mDrawerViewControl = new DrawerViewControl(mView, mContext, mRecyclerView, mRecommendList);
        mDrawerViewControl.setDrawerLayoutView();
        refresh();
    }
    
    public void deleteRecommendSchedule(int position) {
        mAdapter.notifyItemRemoved(position);
        mRecommendList.remove(position);
        refresh();
    }
    
    public static void refresh() {
        mAdapter.notifyDataSetChanged();
    }
    
    public FragmentManager getFM() {
        return getSupportFragmentManager();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new PostAdapter(mContext, mRecommendList);
        mRecyclerView.setAdapter(mAdapter);
    
        mDrawerViewControl = new DrawerViewControl(mView, mContext, mRecyclerView, mRecommendList);
        mDrawerViewControl.setDrawerLayoutView();
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            finish();
        }
        if (v.getId() == R.id.refreshBtn) {
            progressBar.setVisibility(View.VISIBLE);
            selectAllRecommendListRx(mHashtag);
        }
    }
    
    // back button 임시 처리
    @Override
    public void onBackPressed() {
        if (mDrawerViewControl == null) {
            super.onBackPressed();
        }
        else if (mDrawerViewControl.mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawerViewControl.closeDrawer();
        }
        else {
            super.onBackPressed();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    
        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            RecommendResponse recommendResponse = data.getParcelableExtra("updatedRecommendPost");
            int position = data.getIntExtra("position", 0);
            mRecommendList.set(position, recommendResponse);
            mAdapter.notifyDataSetChanged();
            mDrawerViewControl.updateDrawerUI(recommendResponse);
        }
    }
    
    public UserResponse getUser() {
        return userResponse;
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
                        userResponse = response;
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