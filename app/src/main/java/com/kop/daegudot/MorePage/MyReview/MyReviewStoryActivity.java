package com.kop.daegudot.MorePage.MyReview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kop.daegudot.Network.More.MyInfo.MyRecommendList;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.DrawerViewControl;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * 더보기 - 내가 쓴 글
 * 추천의 글 목록과 똑같이 동작하지만 서버에서 받아오는 부분만 다름
 */
public class MyReviewStoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyReviewStoryActivity";
    private Context mContext;
    private ArrayList<RecommendResponse> mRecommendList;
    private RecyclerView mRecyclerView;
    public MyReviewAndCommentAdapter mMyReviewAndCommentAdapter;
    View mView;
    
    ProgressBar mProgressBar;
    public DrawerViewControl mDrawerViewControl;
    
    /* Rx java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review_story);
        this.mContext = this;
//        mView = LayoutInflater.from(this).inflate(R.layout.activity_my_review_story, null);
        mView = findViewById(R.id.my_review_layout);
        
        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
    
        TextView title = findViewById(R.id.title);
        title.setText("내가 쓴 글");
        
        ImageButton backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(this);
        mProgressBar = findViewById(R.id.progress_bar);

//        prepareMenu();
        selectMyRecommendScheduleListRx();
        
        mRecyclerView = findViewById(R.id.my_review_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        
    }
    
    private void selectMyRecommendScheduleListRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<MyRecommendList> observable = service.selectMyRecommendSchedules();
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MyRecommendList>() {
                    @Override
                    public void onNext(MyRecommendList response) {
                        Log.d("RX " + TAG, "Next");
                        if (response.status == 1L) {
                            mRecommendList = response.recommendScheduleResponseDtoArrayList;
                            
                            for (int i = 0; i < mRecommendList.size(); i++) {
                                Log.d("RX " + TAG, "i: " + mRecommendList.get(i).title);
                            }
                        }
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");
                        
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
    
    // TODO: 지우기
    private void prepareMenu(){
        mRecommendList = new ArrayList<>();
        
        // TODO: DB에서 작성자의 글 가져오기
    
        String[] title = {"제목1", "제목2", "제목3", "제목4"};
        float[] rating = { 1, (float) 1.5, 2, (float) 4.5};
        String[] writer = {"작성자1", "작성자1", "작성자1", "작성자1"};
        int[] commentN = {2, 5, 3, 1};
        String[] content = {"내용1", "내용2", "내용3", "내용4"};
    
        for (int i = 0; i < 4; i++) {
            RecommendResponse data = new RecommendResponse();
            data.title = title[i];
            data.star = rating[i];
            data.content = content[i];
            data.id = i;
//            data.setWriter(writer[i]);
//            data.setCommentNum(commentN[i]);
    
            mRecommendList.add(data);
        }
    }
    
    public FragmentManager getFM() {
        return getSupportFragmentManager();
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
