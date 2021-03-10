package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleInfo;
import com.kop.daegudot.Network.Place;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class PlaceBottomSheet implements Button.OnClickListener {
    private final static String TAG = "PlaceBottomSheet";
    private Context mContext;
    private ArrayList<Place> mPlaceList = null;
    MainScheduleInfo mMainSchedule;
    ArrayList<SubScheduleInfo> mSubScheduleList;
    
    private int mTag;
    private Button mHeartBtn;
    private Button mAddToSchBtn;
    
    PlaceBottomSheet(Context context,
                     MainScheduleInfo mainSchedule, ArrayList<SubScheduleInfo> subScheduleList) {
        mContext = context;
        mMainSchedule = mainSchedule;
        mSubScheduleList = subScheduleList;
    
        mHeartBtn = ((MapMainActivity)mContext).findViewById(R.id.heart_btn);
        mHeartBtn.setOnClickListener(this);
        mAddToSchBtn = ((MapMainActivity)mContext).findViewById(R.id.addToSch_btn);
        mAddToSchBtn.setOnClickListener(this);
    }
    
    public void changePlaceBottomSheet(int tag) {
        mTag = tag;
        
        TextView title = ((MapMainActivity)mContext).findViewById(R.id.tv_title);
        TextView address = ((MapMainActivity)mContext).findViewById(R.id.tv_address);
        TextView summary = ((MapMainActivity)mContext).findViewById(R.id.tv_spec);
        RatingBar rating = ((MapMainActivity)mContext).findViewById(R.id.rating_bar);
        
        updatePlaceList();
        Place item = mPlaceList.get(tag);
        
        title.setText(item.attractName);
        address.setText(item.address);
//        summary.setText(item.getAttractContents());
        summary.setText("너무 길어서 일단 안보이게 하게쑵니다");
        rating.setRating(item.rate);
        
        if (mPlaceList.get(mTag).like) {
            mHeartBtn.setBackgroundResource(R.drawable.full_heart);
        } else {
            mHeartBtn.setBackgroundResource(R.drawable.heart);
        }
    }
    
    public void updatePlaceList() {
        mPlaceList = ((MapMainActivity)mContext).updatePlaceList();
    }
    
    public void addToSubscheduleList() {
        
        int days = mMainSchedule.getDateBetween(); // mSubSchedule.size()
        final String[] list = new String[days];
        
        for(int i = 0; i < days; i++) {
            list[i] = (i + 1) + "일차";
        }
    
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
    
        final int[] checkedItem = {-1};
        builder.setTitle("날짜를 선택하세요")
                .setSingleChoiceItems(list, checkedItem[0], (dialog, which) -> checkedItem[0] = which)
                .setPositiveButton("선택", (dialog, which) -> {
                    int position = checkedItem[0];
    
                    Log.i(TAG, "선택한 날짜 position: " + position);
                    
                    // position으로 선택한 날짜를, tag로 marker를 알 수 있게 함
                    ((MapMainActivity)mContext).adapterChange(position, mTag);
                    
                    dialog.dismiss();
                })
                .setNegativeButton("취소", (dialog, which) -> dialog.dismiss())
                .show();
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.heart_btn:
                if (mPlaceList.get(mTag).like) { // 좋아요 했으면 취소
                    mHeartBtn.setBackgroundResource(R.drawable.heart);
                    mPlaceList.get(mTag).like = false;
                } else {    // 좋아요 안했으면 좋아요
                    mHeartBtn.setBackgroundResource(R.drawable.full_heart);
                    mPlaceList.get(mTag).like = true;
                }
                break;
            case R.id.addToSch_btn:
                addToSubscheduleList();
        }
    }
}
