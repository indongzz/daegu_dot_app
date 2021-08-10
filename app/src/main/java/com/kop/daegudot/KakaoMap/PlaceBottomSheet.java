package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kop.daegudot.Login.KakaoLogin.GlobalApplication;
import com.kop.daegudot.MorePage.MyWishlist.Database.Wishlist;
import com.kop.daegudot.MorePage.MyWishlist.WishlistDBHandler;
import com.kop.daegudot.MySchedule.DateSubSchedule;
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.Network.Schedule.SubScheduleRegister;
import com.kop.daegudot.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 장소 마커 클릭 시 뜨는 BottomSheet
 *
 */
public class PlaceBottomSheet implements Button.OnClickListener {
    private final static String TAG = "PlaceBottomSheet";
    private Context mContext;
    private ArrayList<Place> mPlaceList = null;
    private Place mPlace = null;
    MainScheduleInfo mMainSchedule;
    ArrayList<DateSubSchedule> mDateSubScheduleList;
    
    private int mTag;
    private Button mHeartBtn;
    private Button mAddToSchBtn;
    private TextView mTitle;
    private TextView mAddress;
    private TextView mContents;
    private RatingBar mStar;
    
    PlaceBottomSheet(Context context,
                     MainScheduleInfo mainSchedule, ArrayList<DateSubSchedule> subScheduleList) {
        mContext = context;
        mMainSchedule = mainSchedule;
        mDateSubScheduleList = subScheduleList;
    
        bindViews();
    }
    
    public void bindViews() {
        mHeartBtn = ((MapMainActivity)mContext).findViewById(R.id.heart_btn);
        mHeartBtn.setOnClickListener(this);
        mAddToSchBtn = ((MapMainActivity)mContext).findViewById(R.id.addToSch_btn);
        mAddToSchBtn.setOnClickListener(this);
    
        mTitle = ((MapMainActivity)mContext).findViewById(R.id.tv_title);
        mAddress = ((MapMainActivity)mContext).findViewById(R.id.tv_address);
        mContents = ((MapMainActivity)mContext).findViewById(R.id.tv_spec);
        mStar = ((MapMainActivity)mContext).findViewById(R.id.rating_bar);
    }
    
    public void changePlaceBottomSheet(int tag) {
        mTag = tag;
     
        updatePlaceList();
    
        for (Place o : mPlaceList) {
            if (o.id == tag) {
                mPlace = o;
            }
        }
    
        mTitle.setText(mPlace.attractName);
        mAddress.setText(mPlace.address);
//        summary.setText(mPlace.getAttractContents());
        mContents.setText("너무 길어서 일단 안보이게 하게쑵니다");
        mStar.setRating(mPlace.rate);
    
        if (mPlace.like) {
            mHeartBtn.setBackgroundResource(R.drawable.full_heart);
        } else {
            mHeartBtn.setBackgroundResource(R.drawable.heart);
        }
        
    }
    
    public void updatePlaceList() {
        mPlaceList = ((MapMainActivity)mContext).updatePlaceList();
    }
    
    public void selectSubScheduleDate() {
        int days = mMainSchedule.getDateBetween();
        final String[] list = new String[days];
        
        LocalDate[] dateArray = mMainSchedule.getDateArray();
        
        for (int i = 0; i < dateArray.length; i++) {
            list[i] = dateArray[i].format(DateTimeFormatter.ofPattern("MM.dd"));
        }
    
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
    
        final int[] checkedItem = {-1};
        builder.setTitle("날짜를 선택하세요");
        builder.setSingleChoiceItems(list, checkedItem[0], (dialog, which) -> checkedItem[0] = which);
        builder.setPositiveButton("선택", (dialog, which) -> {
            int position = checkedItem[0];
        
            // position으로 선택한 날짜를 알 수 있음
            addSubSchedule(dateArray[position].toString());
        
            dialog.dismiss();
        });
        builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    
    public void addSubSchedule(String date) {
        /* update Place list */
        mPlaceList = ((MapMainActivity) mContext).updatePlaceList();
        
        Log.d(TAG, "add subschedule: " + mPlace.id);
        SubScheduleRegister subScheduleRegister = new SubScheduleRegister();
        subScheduleRegister.mainScheduleId = mMainSchedule.getMainId();
        subScheduleRegister.date = date;
        subScheduleRegister.placesId = mPlace.id;
        
        SubScheduleHandler subScheduleHandler = new SubScheduleHandler(mContext);
        subScheduleHandler.registerSubSchedule(subScheduleRegister, mPlace);
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.heart_btn) {
            Wishlist wishlist = new Wishlist();
            wishlist.placeId = mPlace.id;
            wishlist.address = mPlace.address;
            wishlist.attractName = mPlace.attractName;
            wishlist.star = mPlace.rate;
            
            if (mPlace.like) { // 좋아요 했으면 취소
                mHeartBtn.setBackgroundResource(R.drawable.heart);
                mPlace.like = false;
                
                WishlistDBHandler wishlistDBHandler = new WishlistDBHandler(mContext);
                wishlistDBHandler.deleteWishlists(wishlist);
            }
            else {    // 좋아요 안했으면 좋아요
                mHeartBtn.setBackgroundResource(R.drawable.full_heart);
                mPlace.like = true;
                
                WishlistDBHandler wishlistDBHandler = new WishlistDBHandler(mContext);
                wishlistDBHandler.insertWishlist(wishlist);
            }
        }
        
        if (v.getId() == R.id.addToSch_btn) {
            selectSubScheduleDate();
        }
    }
    
}
