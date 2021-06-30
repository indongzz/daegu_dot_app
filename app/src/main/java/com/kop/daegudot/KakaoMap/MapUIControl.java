package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.kop.daegudot.R;

public class MapUIControl {
    Context mContext;
    View mView;
    
    Button[] mCategory;
    int current = 0;
    
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
        String[] catString = {"명소", "숙박", "음식", "카페"};
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
            mCategory[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeButtonUI(next);
                    current = next;
                    ((MapMainActivity)mContext).changeCategory(current);
                }
            });
            layout.addView(mCategory[i]);
        }
    }
    
    public void changeButtonUI(int next) {
        mCategory[current].setTextColor(ContextCompat.getColor(mContext, R.color.black));
        mCategory[current].setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_line_btn));
        mCategory[next].setTextColor(ContextCompat.getColor(mContext, R.color.white));
        mCategory[next].setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_blue));
    }
}
