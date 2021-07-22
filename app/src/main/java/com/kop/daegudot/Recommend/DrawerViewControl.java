package com.kop.daegudot.Recommend;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.R;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 *  DrawerLayout의 View 컨트롤
 *  Drawer open, close, listener
 */
public class DrawerViewControl {
    public static String TAG = "DrawerViewControl";
    View mView;
    Context mContext;
    private ArrayList<RecommendResponse> mRecommendList;
    private RecyclerView mRecyclerView;
    
    public DrawerLayout mDrawer;
    public DrawerHandler mDrawerHandler;
    
    public DrawerViewControl(View view, Context context, RecyclerView recyclerView, ArrayList<RecommendResponse> recommendList) {
        mView = view;
        mContext = context;
        mRecyclerView = recyclerView;
        mRecommendList = recommendList;
    }
    
    public void setDrawerLayoutView() {
        // 작성한 글 Drawerlayout으로 띄우기
        mDrawer = mView.findViewById(R.id.drawer);
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
    
    public void openDrawer(RecommendResponse recommendResponse, int position) {
        mDrawer.openDrawer(GravityCompat.END);
        mDrawerHandler = new DrawerHandler(mContext, mView, recommendResponse, position);
        mDrawerHandler.setDrawer();
    }
    
    public void closeDrawer() {
        mDrawer.closeDrawer(GravityCompat.END);
        InputMethodManager keyboardManager =
                (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        keyboardManager.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }
}
