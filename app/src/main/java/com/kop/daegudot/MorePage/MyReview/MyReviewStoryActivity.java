package com.kop.daegudot.MorePage.MyReview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.DrawerViewControl;

import java.util.ArrayList;


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
    
    public DrawerViewControl mDrawerViewControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review_story);
        this.mContext = this;
//        mView = LayoutInflater.from(this).inflate(R.layout.activity_my_review_story, null);
        mView = findViewById(R.id.my_review_layout);
        
        TextView title = findViewById(R.id.title);
        title.setText("내가 쓴 글");
        ImageButton backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(this);

        prepareMenu();
        
        mRecyclerView = findViewById(R.id.my_review_list);
        mMyReviewAndCommentAdapter = new MyReviewAndCommentAdapter(mContext, mRecommendList);
        mRecyclerView.setAdapter(mMyReviewAndCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        
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
