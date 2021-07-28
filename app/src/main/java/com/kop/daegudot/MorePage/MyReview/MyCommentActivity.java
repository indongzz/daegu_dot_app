package com.kop.daegudot.MorePage.MyReview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kop.daegudot.MorePage.MyReview.MyReviewAndCommentAdapter;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.DrawerViewControl;

import java.util.ArrayList;

public class MyCommentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MyCommentActivity";
    private Context mContext;
    View mView;
    private ArrayList<RecommendResponse> mRecommendList;
    private RecyclerView mRecyclerView;
    public MyReviewAndCommentAdapter mMyReviewAndCommentAdapter;
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
        mMyReviewAndCommentAdapter = new MyReviewAndCommentAdapter(mContext, mRecommendList);
        mRecyclerView.setAdapter(mMyReviewAndCommentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    
        mDrawerViewControl = new DrawerViewControl(mView, mContext, mRecyclerView, mRecommendList);
        mDrawerViewControl.setDrawerLayoutView();
    }
    
    private void prepareMenu(){
        mRecommendList = new ArrayList<>();
        
        // TODO: 작성자의 글 가져오기
        
        String[] title = {"제목1", "제목2", "제목3", "제목4"};
        float[] rating = { 1, (float) 1.5, 2, (float) 4.5};
        String[] writer = {"작성자1", "작성자1", "작성자1", "작성자1"};
        int[] commentN = {2, 5, 3, 1};
        String[] content = {"내용1", "내용2", "내용3", "내용4"};
        
        for (int i = 0; i < 4; i++) {
            RecommendResponse data = new RecommendResponse();
            data.title = title[i];
            data.star = rating[i];
//            data.setWriter(writer[i]);
//            data.setCommentNum(commentN[i]);
            data.content = content[i];
            //TODO : id 할당
            data.id = i;
            
            mRecommendList.add(data);
        }
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            finish();
        }
    }
    
}
