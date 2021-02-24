package com.kop.daegudot.MorePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.PostList;

import java.util.ArrayList;

public class MyReviewStoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyReviewStoryActivity";
    private Context mContext;
    private ArrayList<PostList> mPostList;
    private RecyclerView mRecyclerView;
    private MyReviewAndCommentAdapter mMyReviewAndCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review_story);
        this.mContext = getApplicationContext();

        // TODO: 뒤로가기 버튼 구현 필요
        TextView title = findViewById(R.id.title);
        title.setText("내가 쓴 글");
        ImageButton backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(this);

        prepareMenu();

        mRecyclerView = findViewById(R.id.myreview_list);
        mMyReviewAndCommentAdapter = new MyReviewAndCommentAdapter(mContext, mPostList);
        mRecyclerView.setAdapter(mMyReviewAndCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void prepareMenu(){
        mPostList = new ArrayList<>();
        
        // TODO: 작성자의 글 가져오기
    
        String[] title = {"제목1", "제목2", "제목3", "제목4"};
        float[] rating = { 1, (float) 1.5, 2, (float) 4.5};
        String[] writer = {"작성자1", "작성자1", "작성자1", "작성자1"};
        int[] commentN = {2, 5, 3, 1};
        String[] content = {"내용1", "내용2", "내용3", "내용4"};
    
        for (int i = 0; i < 4; i++) {
            PostList data = new PostList();
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
        switch (v.getId()) {
            case R.id.backBtn:
                finish();
                break;
        }
    }
}
