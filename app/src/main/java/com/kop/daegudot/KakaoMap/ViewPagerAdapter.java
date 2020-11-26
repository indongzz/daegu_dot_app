package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleInfo;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    static Context mContext;
    ArrayList<SubScheduleInfo> mSubScheduleList;
    
    RecyclerView mRecyclerView;
    MainScheduleInfo mMainSchedule;
    
    ViewPagerAdapter(Context context, ArrayList<SubScheduleInfo> subScheduleList) {
        mContext = context;
        mSubScheduleList = subScheduleList;
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
            int position = getAdapterPosition();
            switch(v.getId()) {
                case R.id.left_btn:
                    if (position > 0)
                        ((MapMainActivity) mContext).mViewPager.setCurrentItem(position - 1);
                    break;
                case R.id.right_btn:
                        ((MapMainActivity) mContext).mViewPager.setCurrentItem(position + 1);
                    break;
            }
        }
    }
    
    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
        View view = inflater.inflate(R.layout.bottom_sheet_schedule, parent, false);
        ViewPagerAdapter.ViewHolder vh = new ViewPagerAdapter.ViewHolder(view);
    
        mRecyclerView = view.findViewById(R.id.BSScheduleList);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
    
    
        ScheduleRecyclerViewAdapter adapter = new ScheduleRecyclerViewAdapter(mSubScheduleList.get(0).getPlaceName(), mContext);
    
        mRecyclerView.setAdapter(adapter);
    
        int[] ATTRS = new int[]{android.R.attr.listDivider};
    
        TypedArray a = mContext.obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        divider.setAlpha(0);
        int inset = view.getResources().getDimensionPixelSize(R.dimen.recyclerView_margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        
        a.recycle();
    
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        itemDecoration.setDrawable(insetDivider);
        
        mRecyclerView.addItemDecoration(itemDecoration);
    
//        float width = (float) 320 / mSubScheduleList.get(0).getPlaceName().size() - 2;
//        mRecyclerView.addItemDecoration(new RecyclerViewDecoration(dpToPx(mContext, width)));
        
        return vh;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = (position + 1) + " 일차";
        holder.nthday.setText(text);
    
        if (position == 0) {
            holder.leftBtn.setBackgroundResource(R.drawable.blank_arrow_left);
            holder.rightBtn.setBackgroundResource(R.drawable.arrow_right);
        } else if (position == getItemCount() - 1) {
            holder.leftBtn.setBackgroundResource(R.drawable.arrow_left);
            holder.rightBtn.setBackgroundResource(R.drawable.blank_arrow_right);
        } else {
            holder.leftBtn.setBackgroundResource(R.drawable.arrow_left);
            holder.rightBtn.setBackgroundResource(R.drawable.arrow_right);
        }
    }
    
    @Override
    public int getItemCount() {
        return mSubScheduleList.size();
    }
    
    // change dp to pixel
    public int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}

// RecyclerView 사이 간격 조절
class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
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

}