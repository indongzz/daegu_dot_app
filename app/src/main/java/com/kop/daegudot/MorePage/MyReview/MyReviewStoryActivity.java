package com.kop.daegudot.MorePage.MyReview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.DrawerHandler;
import com.kop.daegudot.Recommend.DrawerViewControl;
import com.kop.daegudot.Recommend.PostItem;

import java.util.ArrayList;


/**
 * 더보기 - 내가 쓴 글
 * 추천의 글 목록과 똑같이 동작하지만 서버에서 받아오는 부분만 다름
 */
public class MyReviewStoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyReviewStoryActivity";
    private Context mContext;
    private ArrayList<PostItem> mPostList;
    private RecyclerView mRecyclerView;
    private MyReviewAndCommentAdapter mMyReviewAndCommentAdapter;
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
        mMyReviewAndCommentAdapter = new MyReviewAndCommentAdapter(mContext, mPostList);
        mRecyclerView.setAdapter(mMyReviewAndCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        
        mDrawerViewControl = new DrawerViewControl(mView, mContext, mRecyclerView, mPostList);
        mDrawerViewControl.setDrawerLayoutView();
    }

    // TODO: 지우기
    private void prepareMenu(){
        mPostList = new ArrayList<>();
        
        // TODO: DB에서 작성자의 글 가져오기
    
        String[] title = {"제목1", "제목2", "제목3", "제목4"};
        float[] rating = { 1, (float) 1.5, 2, (float) 4.5};
        String[] writer = {"작성자1", "작성자1", "작성자1", "작성자1"};
        int[] commentN = {2, 5, 3, 1};
        String[] content = {"내용1", "내용2", "내용3", "내용4"};
    
        for (int i = 0; i < 4; i++) {
            PostItem data = new PostItem();
            data.setTitle(title[i]);
            data.setRating(rating[i]);
            data.setWriter(writer[i]);
            data.setCommentNum(commentN[i]);
            data.setContent(content[i]);
            //TODO : id 할당
            data.setId(i);
    
            Log.i(TAG, "comment: " + data.getCommentString());
    
            mPostList.add(data);
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
