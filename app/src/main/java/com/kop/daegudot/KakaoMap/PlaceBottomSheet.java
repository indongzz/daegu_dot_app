package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleInfo;
import com.kop.daegudot.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PlaceBottomSheet implements Button.OnClickListener {
    private final static String TAG = "PlaceBottomSheet";
    private Context mContext;
    private ArrayList<MarkerInfo> mMarkerList;
    MainScheduleInfo mMainSchedule;
    ArrayList<SubScheduleInfo> mSubScheduleList;
    
    private int mTag;
    private Button mHeartBtn;
    private Button mAddToSchBtn;
    
    PlaceBottomSheet(Context context, ArrayList<MarkerInfo> markerItems,
                     MainScheduleInfo mainSchedule, ArrayList<SubScheduleInfo> subScheduleList) {
        mContext = context;
        mMarkerList = markerItems;
        
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
        
        MarkerInfo item = mMarkerList.get(tag);
        
        title.setText(item.getName());
        address.setText(item.getAddress());
        summary.setText(item.getSummary());
        rating.setRating(item.getRate());
        
        if (mMarkerList.get(mTag).isLiked()) {
            mHeartBtn.setBackgroundResource(R.drawable.full_heart);
        } else {
            mHeartBtn.setBackgroundResource(R.drawable.heart);
        }
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.heart_btn:
                if (mMarkerList.get(mTag).isLiked()) { // 좋아요 했으면 취소
                    mHeartBtn.setBackgroundResource(R.drawable.heart);
                    mMarkerList.get(mTag).setLike(false);
                } else {    // 좋아요 안했으면 좋아요
                    mHeartBtn.setBackgroundResource(R.drawable.full_heart);
                    mMarkerList.get(mTag).setLike(true);
                }
                break;
            case R.id.addToSch_btn:
                addToSubscheduleList();
        }
    }
    
    public void addToSubscheduleList() {
        final MarkerInfo markerInfo = mMarkerList.get(mTag);
        
        int days = mMainSchedule.getDateBetween(); // mSubSchedule.size()
        final String[] list = new String[days];
        
        for(int i = 0; i < days; i++) {
            list[i] = (i + 1) + "일차";
        }
    
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
    
        final int[] checkedItem = {-1};
        builder.setTitle("날짜를 선택하세요")
                .setSingleChoiceItems(list, checkedItem[0], new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItem[0] = which;
                    }
                })
                .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: add to Subschedule list
                        int position = checkedItem[0];
                        
                        Log.i(TAG, "선택한 날짜 position: " + position);
                        SubScheduleInfo subScheduleInfo = mSubScheduleList.get(position);
                        
                        //place name
                        ArrayList<String> placeName = subScheduleInfo.getPlaceName();
                        placeName.add(markerInfo.getName());
                        
                        //place address
                        ArrayList<String> address = subScheduleInfo.getAddress();
                        address.add(markerInfo.getAddress());
                        
                        Log.i("PlaceBottomSheet", mSubScheduleList.get(position).getDate());
                        mSubScheduleList.get(position).setAddress(address);
                        mSubScheduleList.get(position).setPlaceName(placeName);
                        
                        ((MapMainActivity)mContext).adapterChange(position);
                        
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
