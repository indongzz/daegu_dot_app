package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class PlaceBottomSheet implements Button.OnClickListener {
    
    private Context mContext;
    private ArrayList<MarkerInfo> mMarkerItems;
    private int mTag;
    private Button mHeartBtn;
    private Button mAddToSchBtn;
    
    PlaceBottomSheet(Context context, ArrayList<MarkerInfo> markerItems) {
        mContext = context;
        mMarkerItems = markerItems;
    
        mHeartBtn = ((MapMainActivity)mContext).findViewById(R.id.heart_btn);
        mHeartBtn.setOnClickListener(this);
    }
    
    public void changePlaceBottomSheet(int tag) {
        mTag = tag;
        
        TextView title = ((MapMainActivity)mContext).findViewById(R.id.tv_title);
        TextView address = ((MapMainActivity)mContext).findViewById(R.id.tv_address);
        TextView summary = ((MapMainActivity)mContext).findViewById(R.id.tv_spec);
        RatingBar rating = ((MapMainActivity)mContext).findViewById(R.id.rating_bar);
        
        MarkerInfo item = mMarkerItems.get(tag);
        
        title.setText(item.getName());
        address.setText(item.getAddress());
        summary.setText(item.getSummary());
        rating.setRating(item.getRate());
        
        if (mMarkerItems.get(mTag).isLiked()) {
            mHeartBtn.setBackgroundResource(R.drawable.full_heart);
        } else {
            mHeartBtn.setBackgroundResource(R.drawable.heart);
        }
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.heart_btn:
                if (mMarkerItems.get(mTag).isLiked()) { // 좋아요 했으면 취소
                    mHeartBtn.setBackgroundResource(R.drawable.heart);
                    mMarkerItems.get(mTag).setLike(false);
                } else {    // 좋아요 안했으면 좋아요
                    mHeartBtn.setBackgroundResource(R.drawable.full_heart);
                    mMarkerItems.get(mTag).setLike(true);
                }
                break;
            case R.id.addToSch_btn:
                // TODO: 일정에 추가 클릭 시 일정에 추가하기
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
//
//                builder.setTitle("날짜를 선택하세요")
//                        .setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener(){
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNeutralButton("선택", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .show();
        }
    }
}
