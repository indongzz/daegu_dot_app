package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleInfo;
import com.kop.daegudot.R;

import java.util.ArrayList;


public class ScheduleBottomSheet {
    private Context mContext;
    MainScheduleInfo data;
    SubScheduleInfo itidata;
    Button[] mButtons;
    
    float oldX;
    
    ScheduleBottomSheet(Context context) {
        mContext = context;
    
        data = new MainScheduleInfo();
        String firstdate = "20.11.14";
        String lastDate = "20.11.16";
        data.setmFirstDate(firstdate);
        data.setmLastDate(lastDate);
        data.setmDDate();
        int days = data.getDateBetween();
    
        RecyclerView recyclerView = ((MapMainActivity) mContext).findViewById(R.id.BSScheduleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        
        // TODO: get list of subschedule address name
        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("요깅1");
        nameList.add("요깅2");
        nameList.add("요깅3");
        nameList.add("요깅4");
        nameList.add("요깅5");
        nameList.add("요깅6");
        
        recyclerView.setLayoutManager(layoutManager);
        ScheduleBSAdapter adapter = new ScheduleBSAdapter(nameList, mContext);
        
        recyclerView.setAdapter(adapter);
     //   setmButtons(data.getDateBetween());
        
        float width = (float) 320 / nameList.size();
        recyclerView.addItemDecoration(new RecyclerViewDecoration(dpToPx(mContext, width)));
    }
    
    public void ViewScheduleBottomSheet() {
    
    }
    
    public void setmButtons(int days) {
        mButtons = new Button[days];
    
        CoordinatorLayout layout = (CoordinatorLayout) ((MapMainActivity) mContext).findViewById(R.id.schedule_bottomSheet_layout);
        
//        for (int i = 0; i < days; i++) {
//            mButtons[i] = new Button(mContext);
//            mButtons[i].setBackgroundResource(R.drawable.buttonsheet_btn);
//            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(30, 30);
//            lp.startToEnd = R.id.left_btn;
//            lp.bottomToBottom = R.id.schedule_bottomSheet_layout;
//            lp.topToTop = R.id.schedule_bottomSheet_layout;
//            lp.setMarginStart(dpToPx(mContext, 300) / (data.getDateBetween() - 1) * i);
//            mButtons[i].setLayoutParams(lp);  // 버튼 위치와 크기
//            mButtons[i].setStateListAnimator(null);  // 그림자 지우기
//
//            mButtons[i].setOnTouchListener(this);
//            layout.addView(mButtons[i]);
//        }
    }
    
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int width = ((ViewGroup)v.getParent()).getWidth() - v.getWidth();
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            oldX = event.getX();
//
//            Log.i("Schedule Bottom sheet", "Action Down x" + event.getRawX());
//        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            v.setX(event.getRawX() - oldX);
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//
//        }
//        return false;
//    }
    
    public int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}

// RecyclerView 사이 간격 조절
class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
    private final int divWidth;
    
    public RecyclerViewDecoration(int divWidth) {
        this.divWidth = divWidth;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        
        if (position != parent.getAdapter().getItemCount() -1) {
            outRect.right = divWidth;
        }
    }
}