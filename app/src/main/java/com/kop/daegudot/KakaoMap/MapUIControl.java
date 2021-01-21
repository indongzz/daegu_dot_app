package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kop.daegudot.R;

public class MapUIControl implements View.OnClickListener {
    Context mContext;
    View mView;
    
    Button[] mCategory;
    Button[] mHashTag;
    
    Button mToggleBtn;
    
    int flag = 1;
    
    MapUIControl(Context context, View view) {
        mContext = context;
        mView = view;
    
        mToggleBtn = mView.findViewById(R.id.toggle_btn);
        mToggleBtn.setOnClickListener(this);
    }

    public void setCategoryBtn() {
        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
        int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
        params.setMargins(margin, 0, margin, 0);

        LinearLayout layout = mView.findViewById(R.id.linear_layout);
        String[] catString = {"카테고리1", "카테고리2", "카테고리3", "카테고리4", "카테고리5"};
        int size = catString.length;

        mCategory = new Button[size];

        for (int i = 0; i < size; i++) {
            mCategory[i] = new Button(mContext);
            mCategory[i].setText(catString[i]);
            mCategory[i].setTextSize(12);
            mCategory[i].setLayoutParams(params);
            mCategory[i].setPadding(1, 1, 1, 1);
            mCategory[i].setBackground(mContext.getDrawable(R.drawable.round_line_btn));
            layout.addView(mCategory[i]);
        }
    }
    
    
    public void setHashBtn() {
        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
        int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics());
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
        params.setMargins(margin, 0, margin, 0);
        
        LinearLayout layout = mView.findViewById(R.id.linear_layout);
        String[] catString = {"해시태그1", "해시태그2", "해시태그3", "해시태그4", "해시태그5"};
        int size = catString.length;
        
        mHashTag = new Button[size];
        
        for (int i = 0; i < size; i++) {
            mHashTag[i] = new Button(mContext);
            mHashTag[i].setText(catString[i]);
            mHashTag[i].setTextSize(12);
            mHashTag[i].setLayoutParams(params);
            mHashTag[i].setPadding(1, 1, 1, 1);
            mHashTag[i].setBackground(mContext.getDrawable(R.drawable.round_line_btn));
            layout.addView(mHashTag[i]);
            
            mHashTag[i].setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle_btn:
                if (flag == 1) {
                    for (Button button : mCategory) {
                        button.setVisibility(View.GONE);
                    }
                    for (Button button : mHashTag) {
                        button.setVisibility(View.VISIBLE);
                    }
                    mToggleBtn.setText("카테고리");
                    flag = 0;
                } else {
                    for (Button button : mCategory) {
                        button.setVisibility(View.VISIBLE);
                    }
                    for (Button button : mHashTag) {
                        button.setVisibility(View.GONE);
                    }
                    mToggleBtn.setText("#해시태그");
                    flag = 1;
                }
                break;
        }
    }
}
