package com.kop.daegudot.KakaoMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kop.daegudot.MySchedule.MainScheduleAdapter;
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleInfo;
import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapMainActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, View.OnClickListener {
    private static final String LOG_TAG = "MapMainActivity";
    private MapView mMapView;
    Button[] mCategory;
    Button[] mHashTag;
    Button mToggleBtn;
    ImageButton mBackBtn;
    int flag = 1;
    
    ArrayList<MarkerInfo> mMarkerItems;
    private BottomSheetBehavior mBSBPlace;
    PlaceBottomSheet placeBottomSheet;
    private BottomSheetBehavior mBSBSchedule;
    ScheduleBottomSheet scheduleBottomSheet;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        
        TextView title = findViewById(R.id.title);
        title.setText("장소 검색");
        
        mMapView = findViewById(R.id.map_view);
    
        mMapView.setPOIItemEventListener(this);
        mMapView.setMapViewEventListener(this);
        mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.871344, 128.601705), true);
        mMapView.setZoomLevel(6, true);
    
        mBackBtn = findViewById(R.id.backBtn);
        mBackBtn.setOnClickListener(this);
        mToggleBtn = findViewById(R.id.toggle_btn);
        mToggleBtn.setOnClickListener(this);
        
        setCategoryBtn();
        setHashBtn();
        
        Intent intent = getIntent();
        ArrayList<MainScheduleInfo> mMainScheduleList = intent.getParcelableArrayListExtra("MainScheduleList");
        ArrayList<SubScheduleInfo> mSubScheduleList = intent.getParcelableArrayListExtra("SubScheduleList");
        
        
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        if (mMainScheduleList != null) {
            for (Object a : mMainScheduleList) {
                System.out.println(((MainScheduleInfo)a).getmFirstDate());
            }
        }
        
        if (mSubScheduleList != null) {
            SubScheduleInfo ssi = mSubScheduleList.get(0);
            ArrayList<String> list = ssi.getAddress();
            System.out.println(list.get(0));
        }
        
        
        String[] address = {
                "대구광역시 중구 남산로 4길 112",
                "대구광역시 중구 서성로 6-1",
                "대구광역시 동구동화사 1길 1",
                "대구광역시 중구 국채보상로 670",
                "대구광역시 중구 동성로 2길 80"
        };
        String[] attractname = { "성모당", "서상돈 고택", "동화사 보사계 유공비", "국채보상운동기념공원", "2.28기념중앙공원" };
        String[] tel = {
                "053-250-3000", "053-256-3762",
                "53-982-0101-2", "053-254-9401",
                "053-254-9405"
        };
        
        // 나중에 data read후에 markerinfo에 담기
         mMarkerItems = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            MarkerInfo markerItem = new MarkerInfo(this);
            markerItem.setAddress(address[i]);
            markerItem.setName(attractname[i]);
            markerItem.setTel(tel[i]);
            markerItem.setRate((float) (i+0.5));
            markerItem.setLike(false);
            mMarkerItems.add(markerItem);
        }
        
        for (int i = 0; i< 5; i++) {
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(mMarkerItems.get(i).getName());
            marker.setTag(i);
            marker.setMapPoint(mMarkerItems.get(i).getAddressPoint());
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.blue_pin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageAnchor(0.5f, 1.0f);
            marker.setCustomSelectedImageResourceId(R.drawable.big_yellow_pin);
            marker.setShowCalloutBalloonOnTouch(false);
            mMapView.addPOIItem(marker);
        }
        
        placeBottomSheet = new PlaceBottomSheet(this, mMarkerItems);
        
        CoordinatorLayout placeLayout = (CoordinatorLayout) findViewById(R.id.bottomSheet);
        mBSBPlace = BottomSheetBehavior.from(placeLayout);
        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);
        
//        mBSBPlace.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
        
        scheduleBottomSheet = new ScheduleBottomSheet(this);
        CoordinatorLayout scheduleLayout = (CoordinatorLayout) findViewById(R.id.schedule_bottomSheet_layout);
        mBSBSchedule = BottomSheetBehavior.from(scheduleLayout);
        
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
        placeBottomSheet.changePlaceBottomSheet(mapPOIItem.getTag());
        mBSBPlace.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBSBSchedule.setState(BottomSheetBehavior.STATE_HIDDEN);
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
            case R.id.backBtn:
                finish();
                break;
        }
    }
    
    @Override
    public void onMapViewInitialized(MapView mapView) {
    
    }
    
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    
    }
    
    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
    
    }
    
    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBSBSchedule.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    
    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    
    }
    
    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    
    }
    
    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    
    }
    
    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    
    }
    
    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    
    }
}
