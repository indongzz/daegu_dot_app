package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.MySchedule.DateSubSchedule;
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class MainScheduleBottomSheetAdapter extends RecyclerView.Adapter<MainScheduleBottomSheetAdapter.ViewHolder> {
    private static final String TAG = "MainScheduleBottomSheetAdapter";
    static Context mContext;
    ArrayList<DateSubSchedule> mDateSubScheduleList;
    
    RecyclerView mRecyclerView;
    static MainScheduleInfo mMainSchedule;
    SubScheduleBottomSheetAdapter mSubScheduleBottomSheetAdapter;
    
    MainScheduleBottomSheetAdapter(Context context, MainScheduleInfo mainSchedule, ArrayList<DateSubSchedule> subScheduleList) {
        mContext = context;
        mDateSubScheduleList = subScheduleList;
        mMainSchedule = mainSchedule;
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nthday;
        static Button leftBtn;
        static Button rightBtn;
        
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nthday = itemView.findViewById(R.id.tv_days);
            leftBtn = itemView.findViewById(R.id.left_btn);
            rightBtn = itemView.findViewById(R.id.right_btn);
            
            leftBtn.setOnClickListener(this);
            rightBtn.setOnClickListener(this);
        }
    
        @Override
        public void onClick(View v) {
            int position = Integer.parseInt
                    (nthday.getText().toString().replaceAll("[^0-9]", ""));
            
            switch(v.getId()) {
                case R.id.left_btn:
                    if (position > 1) {
                        ((MapMainActivity) mContext).mMainListView.setCurrentItem(position - 2);
                    }
                    break;
                case R.id.right_btn:
                    if (position < mMainSchedule.getDateBetween()) {
                        ((MapMainActivity) mContext).mMainListView.setCurrentItem(position);
                    }
                    break;
            }
        }
    }
    
    @NonNull
    @Override
    public MainScheduleBottomSheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
        View view = inflater.inflate(R.layout.bottom_sheet_schedule, parent, false);
        MainScheduleBottomSheetAdapter.ViewHolder vh = new MainScheduleBottomSheetAdapter.ViewHolder(view);
        
        mRecyclerView = view.findViewById(R.id.BSScheduleList);
        mRecyclerView.getLayoutParams().width = 830;
        
        // 자식 recyclerview 가 scroll 할 때 부모가 같이 scroll 하는 것을 방지
        RecyclerView.OnItemTouchListener mScrollTouchListener =
                new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
    
        mRecyclerView.addOnItemTouchListener(mScrollTouchListener);
        
        int[] ATTRS = new int[]{android.R.attr.listDivider};

        TypedArray a = mContext.obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        divider.setAlpha(0);
        int inset = 10;
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);

        a.recycle();

        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(insetDivider);

        mRecyclerView.addItemDecoration(itemDecoration);
        
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        
        return vh;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = (position + 1) + " 일차";
        holder.nthday.setText(text);
        
    
        if (position == 0) {
            Log.d(TAG, "position is 0. blank left");
            ViewHolder.leftBtn.setBackgroundResource(R.drawable.blank_arrow_left);
            ViewHolder.rightBtn.setBackgroundResource(R.drawable.arrow_right);
        } else if (position == mMainSchedule.getDateBetween() - 1) {
            Log.d(TAG, "position is last. blank right");
            ViewHolder.leftBtn.setBackgroundResource(R.drawable.arrow_left);
            ViewHolder.rightBtn.setBackgroundResource(R.drawable.blank_arrow_right);
        } else {
            Log.d(TAG, "full arrow");
            ViewHolder.leftBtn.setBackgroundResource(R.drawable.arrow_left);
            ViewHolder.rightBtn.setBackgroundResource(R.drawable.arrow_right);
        }
        
        mSubScheduleBottomSheetAdapter = new SubScheduleBottomSheetAdapter(
                mDateSubScheduleList.get(position).subScheduleList, mContext);
    
        mRecyclerView.setAdapter(mSubScheduleBottomSheetAdapter);
    
    //    setRecyclerViewDivider();
       
    }
    
    
    @Override
    public int getItemCount() {
        return mMainSchedule.getDateBetween();
    }
    
//    public void setRecyclerViewDivider() {
//
//        float width = (float) 320 / getItemCount();
//        mRecyclerView.addItemDecoration(new RecyclerViewDecoration(dpToPx(mContext, width)));
//    }
    
//    // change dp to pixel
//    public int dpToPx(Context context, float dp) {
//        return (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
//    }
}

// RecyclerView 사이 간격 조절
//class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
//    private final int divWidth;
//
//    public RecyclerViewDecoration(int divWidth) {
//        this.divWidth = divWidth;
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        int position = parent.getChildAdapterPosition(view);
//
//        if (position != parent.getAdapter().getItemCount() -1) {
//            outRect.right = divWidth;
//        }
//    }
//
//}