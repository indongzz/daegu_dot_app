package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;

import java.util.ArrayList;

public class MapUIControl {
    Context mContext;
    View mView;
    
    Button[] mCategory;
    int current = 0;
    int categoryFlag = 0;
    
    MapUIControl(Context context, View view) {
        mContext = context;
        mView = view;
    }

    public void setCategoryBtn() {
        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
        int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics());
    
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
        params.setMargins(margin, 0, margin, 0);

        LinearLayout layout = mView.findViewById(R.id.linear_layout);
        
        String[] catString = {"명소", "숙박", "음식"};
        int size = catString.length;

        mCategory = new Button[size];

        for (int i = 0; i < size; i++) {
            mCategory[i] = new Button(mContext);
            mCategory[i].setId(1000+i);
            mCategory[i].setText(catString[i]);
            mCategory[i].setTextSize(12);
            mCategory[i].setLayoutParams(params);
            mCategory[i].setPadding(1, 1, 1, 1);
            mCategory[i].setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_line_btn));
            final int next = i;
            mCategory[i].setOnClickListener(v -> {
                changeButtonUI(next);
                changeCategory(next);
                current = next;
            });
            layout.addView(mCategory[i]);
        }
        
        changeButtonUI(0);
    }
    
    public void changeButtonUI(int next) {
        mCategory[current].setTextColor(ContextCompat.getColor(mContext, R.color.black));
        mCategory[current].setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_line_btn));
        mCategory[next].setTextColor(ContextCompat.getColor(mContext, R.color.white));
        mCategory[next].setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_blue));
    }
    
    public void changeCategory(int category) {
        ((MapMainActivity) mContext).mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);
        ((MapMainActivity) mContext).mBSBSchedule.setState(BottomSheetBehavior.STATE_EXPANDED);
        ((MapMainActivity) mContext).prevPOIItem = null;
        categoryFlag = category;
        
        ((MapMainActivity) mContext).addPOItoMapView();
    }
    
    
    public boolean checkCategory(MapPOIItem item) {
        boolean bool = false;
        
        Place place = new Place();
        
        for (Place o: ((MapMainActivity) mContext).mPlaceList) {
            if (o.id == item.getTag()) {
                place = o;
                break;
            }
        }
        String category = place.category;
        
        // TODO: category 해결
        if (category == null) {
            bool = true;
        }
        else {
            if (categoryFlag == 0 && category.equals("TR")) {
                bool = true;
            } else if (categoryFlag == 1 && category.equals("AD")) {  // 숙박
                bool = true;
            } else if (categoryFlag == 2 && category.equals("FD")) {  // 음식
                bool = true;
            }
        }
        
        return bool;
    }
}
