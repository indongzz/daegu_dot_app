package com.kop.daegudot.MorePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kop.daegudot.MorePage.MyReview.MyReviewAndCommentAdapter;
import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.DrawerViewControl;
import com.kop.daegudot.Recommend.PostItem;

import java.util.ArrayList;

public class MyCommentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyCommentActivity";
    private Context mContext;
    View mView;
    private ArrayList<PostItem> mPostList;
    private RecyclerView mRecyclerView;
    private MyReviewAndCommentAdapter mMyReviewAndCommentAdapter;
    public DrawerViewControl mDrawerViewControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        this.mContext = this;
        mView = findViewById(R.id.my_comment_layout);
        
        prepareMenu();

        TextView title = findViewById(R.id.title);
        title.setText("내가 쓴 댓글");
        ImageButton backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.my_comment_list);
        mMyReviewAndCommentAdapter = new MyReviewAndCommentAdapter(mContext, mPostList);
        mRecyclerView.setAdapter(mMyReviewAndCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    
        mDrawerViewControl = new DrawerViewControl(mView, mContext, mRecyclerView, mPostList);
        mDrawerViewControl.setDrawerLayoutView();
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
    
}
