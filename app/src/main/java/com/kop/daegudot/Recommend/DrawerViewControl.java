package com.kop.daegudot.Recommend;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class DrawerViewControl {
    public static String TAG = "DrawerViewControl";
    View mView;
    Context mContext;
    private ArrayList<PostItem> mPostList;
    private RecyclerView mRecyclerView;
    
    public DrawerLayout mDrawer;
    public DrawerHandler mDrawerHandler;
    
    public DrawerViewControl(View view, Context context, RecyclerView recyclerView, ArrayList<PostItem> postList) {
        mView = view;
        mContext = context;
        mRecyclerView = recyclerView;
        mPostList = postList;
    }
    
    public void setDrawerLayoutView() {
        // 작성한 글 Drawerlayout으로 띄우기
        mDrawer = mView.findViewById(R.id.my_review_drawer);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }
            
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
                int n = Math.min(mRecyclerView.getChildCount(), 10);
                for (int i = 0; i < n; i++) {
                    mRecyclerView.getChildAt(i).setClickable(false);
                }
            }
            
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                int n = Math.min(mRecyclerView.getChildCount(), 10);
                for (int i = 0; i < n; i++) {
                    mRecyclerView.getChildAt(i).setClickable(true);
                }
            }
            
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }
    
    public void openDrawer(int id) {
        PostItem post = null;
        for (PostItem o : mPostList) {
            if (id == o.getId()) {
                post = o;
                break;
            }
        }
        
        if (post != null) {
            mDrawer.openDrawer(GravityCompat.END);
            mDrawerHandler = new DrawerHandler(mContext, mView, post);
            mDrawerHandler.setDrawer();
        }
        else {
            Log.e(TAG, "post: null");
        }
    }
    
    public void closeDrawer() {
        mDrawer.closeDrawer(GravityCompat.END);
        InputMethodManager keyboardManager =
                (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        keyboardManager.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }
}
