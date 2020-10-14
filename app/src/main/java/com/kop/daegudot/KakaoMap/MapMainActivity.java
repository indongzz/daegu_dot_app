package com.kop.daegudot.KakaoMap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapMainActivity extends AppCompatActivity implements MapView.POIItemEventListener, View.OnClickListener {
    private static final String LOG_TAG = "MapMainActivity";
    private MapView mMapView;
    Button[] mCategory;
    Button[] mHashTag;
    Button mToggleBtn;
    int flag = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        
        TextView title = findViewById(R.id.title);
        title.setText("장소 검색");
        
        mMapView = findViewById(R.id.map_view);
    
        mMapView.setPOIItemEventListener(this);
        mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.871344, 128.601705), true);
        mMapView.setZoomLevel(6, true);
    
        mToggleBtn = findViewById(R.id.toggle_btn);
        mToggleBtn.setOnClickListener(this);
        
        setCategoryBtn();
        setHashBtn();
    }
    
    public void setCategoryBtn() {
        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
    
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
        params.setMargins(margin, 0, margin, 0);
    
        LinearLayout layout = findViewById(R.id.linear_layout);
        String[] catString = {"카테고리1", "카테고리2", "카테고리3", "카테고리4", "카테고리5"};
        int size = catString.length;
    
        mCategory = new Button[size];
    
        for (int i = 0; i < size; i++) {
            mCategory[i] = new Button(this);
            mCategory[i].setText(catString[i]);
            mCategory[i].setTextSize(12);
            mCategory[i].setLayoutParams(params);
            mCategory[i].setPadding(1, 1, 1, 1);
            mCategory[i].setBackground(getDrawable(R.drawable.round_line_btn));
            layout.addView(mCategory[i]);
        }
    }
    
    public void setHashBtn() {
        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
        params.setMargins(margin, 0, margin, 0);
        
        LinearLayout layout = findViewById(R.id.linear_layout);
        String[] catString = {"해시태그1", "해시태그2", "해시태그3", "해시태그4", "해시태그5"};
        int size = catString.length;
        
        mHashTag = new Button[size];
        
        for (int i = 0; i < size; i++) {
            mHashTag[i] = new Button(this);
            mHashTag[i].setText(catString[i]);
            mHashTag[i].setTextSize(12);
            mHashTag[i].setLayoutParams(params);
            mHashTag[i].setPadding(1, 1, 1, 1);
            mHashTag[i].setBackground(getDrawable(R.drawable.round_line_btn));
            layout.addView(mHashTag[i]);
            
            mHashTag[i].setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    
    }
    
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    
    }
    
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    
    }
    
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    
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