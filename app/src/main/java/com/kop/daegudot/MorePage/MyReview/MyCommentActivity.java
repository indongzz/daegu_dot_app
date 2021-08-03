package com.kop.daegudot.MorePage.MyReview;

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

import com.kop.daegudot.MorePage.MyReview.MyReviewAndCommentAdapter;
import com.kop.daegudot.Network.More.MyInfo.MyCommentList;
import com.kop.daegudot.Network.Recommend.Comment.CommentResponse;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.DrawerViewControl;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyCommentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyCommentActivity";
    private static final int REQUEST_CODE = 200;
    private Context mContext;
    View mView;
    private ArrayList<RecommendResponse> mRecommendList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    public MyReviewAndCommentAdapter mMyReviewAndCommentAdapter;
    public DrawerViewControl mDrawerViewControl;
    ArrayList<CommentResponse> mCommentList;
    ProgressBar mProgressBar;
    
    /* Rx java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mToken;
    private UserResponse mUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        this.mContext = this;
        mView = findViewById(R.id.my_comment_layout);
        
        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        selectUserByToken();
        
        selectAllMyCommentsRx();
        
        TextView title = findViewById(R.id.title);
        title.setText("내가 쓴 댓글");
        
        ImageButton backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(this);
        mProgressBar = findViewById(R.id.progress_bar);

        mRecyclerView = findViewById(R.id.my_comment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
    
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    
    }
    
    private void selectAllMyCommentsRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<MyCommentList> observable = service.selectMyComments();
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MyCommentList>() {
                    @Override
                    public void onNext(MyCommentList response) {
                        Log.d("RX", "Next");
                        if (response.status == 1L) {
                            mCommentList = response.commentResponseDtoArrayList;
                            
                            mRecommendList = new ArrayList<>();
                            int n = mCommentList.size();
                            for (int i = 0; i < n; i++) {
                                if (!mRecommendList.contains(mCommentList.get(i).recommendScheduleResponseDto))
                                    mRecommendList.add(mCommentList.get(i).recommendScheduleResponseDto);
                            }
                            Collections.sort(mRecommendList);
                        }
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX", e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX", "complete");
                        
                        mProgressBar.setVisibility(View.GONE);
                        updateUI();
                    }
                })
        );
    }
    
    public void updateUI() {
        mMyReviewAndCommentAdapter = new MyReviewAndCommentAdapter(mContext, mRecommendList);
        mRecyclerView.setAdapter(mMyReviewAndCommentAdapter);
    
        mDrawerViewControl = new DrawerViewControl(mView, mContext, mRecyclerView, mRecommendList);
        mDrawerViewControl.setDrawerLayoutView();
    }
    
    public void deleteRecommendSchedule(int position) {
        mMyReviewAndCommentAdapter.notifyItemRemoved(position);
        mRecommendList.remove(position);
    }
    
    public FragmentManager getFM() {
        return getSupportFragmentManager();
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
            mMyReviewAndCommentAdapter.notifyDataSetChanged();
//            mRecyclerView.setAdapter(mMyReviewAndCommentAdapter);
            mDrawerViewControl.updateDrawerUI(recommendResponse);
        }
    }
    
    public UserResponse getUser() {
        return mUser;
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
                        mUser = response;
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
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            finish();
        }
    }
    
    @Override
    public void onBackPressed() {
        if (mDrawerViewControl.mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawerViewControl.mDrawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
