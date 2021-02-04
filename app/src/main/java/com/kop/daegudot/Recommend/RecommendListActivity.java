package com.kop.daegudot.Recommend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class RecommendListActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "RecommendListActivity";
    
    Context mContext;
    ImageButton backBtn;
    RecyclerView mRecyclerView;
    PostAdapter mAdapter;
    
    ArrayList<PostList> mPostList = new ArrayList<>();
    DrawerLayout drawer;
    View mView;
    
    PostScheduleBottomSheet postScheduleBottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mView = getLayoutInflater().inflate(R.layout.activity_recommand_list, null);
        setContentView(mView);
        
        mContext = this;
        
        Log.i(TAG, "list context: " + mContext);
        Intent intent = getIntent();
        
        if (intent.getStringExtra("hashtag") != null) {
            TextView title = findViewById(R.id.title);
            title.setText(intent.getStringExtra("hashtag"));
        }
        
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        
        mRecyclerView = findViewById(R.id.recommend_list_view);
        
        temporarySet();
        
        Log.i(TAG, "size: " + mPostList.size());
        mAdapter = new PostAdapter(mContext, mPostList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        
        // 작성한 글 Drawerlayout으로 띄우기
        drawer = findViewById(R.id.drawer);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
    
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
                int n = Math.min(mRecyclerView.getChildCount(), 10);
                for (int i = 0; i < n; i++) {
                    mRecyclerView.getChildAt(i).setClickable(false);
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
    
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    int n = Math.min(mRecyclerView.getChildCount(), 10);
                for (int i = 0; i < n; i++) {
                    mRecyclerView.getChildAt(i).setClickable(true);
                }
    
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
    
            @Override
            public void onDrawerStateChanged(int newState) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        
        ImageButton backbtn2 = findViewById(R.id.drawer_backBtn);
        backbtn2.setOnClickListener(this);
        TextView drawerTitle = findViewById(R.id.drawer_title);
        drawerTitle.setText("");
        
        postScheduleBottomSheet = new PostScheduleBottomSheet(mContext, mView);
        postScheduleBottomSheet.setViews();
        
        ConstraintLayout bsLayout = findViewById(R.id.schedule_bottomsheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bsLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
    
    public void temporarySet() {
        // TODO: DB에서 해시태그별 목록 가져오기
        String[] title = {"제목1", "제목2", "제목3", "제목4"};
        float[] rating = { 1, (float) 1.5, 2, (float) 4.5};
        String[] writer = {"작성자1", "행갱", "펭귄", "샤스"};
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
    
    public void openDrawer(int id) {
        PostList post = null;
        for (PostList o : mPostList) {
            if (id == o.getId()) {
                post = o;
                break;
            }
        }
        
        if (post != null) {
            Log.i(TAG, "post: " + post.getTitle());
    
            drawer.openDrawer(GravityCompat.END);
            DrawerHandler drawerHandler = new DrawerHandler(mView, post);
            drawerHandler.setDrawer();
            
        }
        else {
            Log.e(TAG, "post: null");
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.drawer_backBtn:
                drawer.closeDrawer(GravityCompat.END);
                break;
        }
    }
    
    // back button 임시 처리
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}