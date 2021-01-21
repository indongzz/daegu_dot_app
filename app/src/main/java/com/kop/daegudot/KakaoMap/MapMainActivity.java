package com.kop.daegudot.KakaoMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleInfo;
import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapMainActivity extends AppCompatActivity implements MapView.MapViewEventListener,
        MapView.POIItemEventListener, View.OnClickListener {
    private static final String TAG = "MapMainActivity";
    private MapView mMapView;
    
    TextView mTitle;    // default = 장소 검색
    ImageButton mBackBtn;
    
    MapMarkerItems mMapMarkerItems;     // set map markerItems
    MapUIControl mMapUIControl;         // to control category and hash tag button
    
    // get Main and sub schedule list from previous activity
    MainScheduleInfo mMainSchedule;
    ArrayList<SubScheduleInfo> mSubScheduleList;
    int position = 0; // default = first page
    
    ArrayList<MarkerInfo> mMarkerItems;
    private BottomSheetBehavior mBSBPlace;
    PlaceBottomSheet placeBottomSheet;
    private BottomSheetBehavior mBSBSchedule;
//    ScheduleBottomSheet scheduleBottomSheet;
    ViewPager2 mViewPager;
    ViewPagerAdapter adapter;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        View view = getWindow().getDecorView();
        
        mTitle = findViewById(R.id.title);
        mTitle.setText("장소 검색");
        
        mMapView = findViewById(R.id.map_view);
    
        mMapView.setPOIItemEventListener(this);
        mMapView.setMapViewEventListener(this);
        mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.871344, 128.601705), true);
        mMapView.setZoomLevel(6, true);
    
        mBackBtn = findViewById(R.id.backBtn);
        mBackBtn.setOnClickListener(this);
        
        // Set Hash Tag button and Category button
        mMapUIControl = new MapUIControl(this, view);
        mMapUIControl.setCategoryBtn();
        mMapUIControl.setHashBtn();
        
        // Set MarkerItems
        mMapMarkerItems = new MapMarkerItems(this, mMapView);
        mMapMarkerItems.setMarkerItems();
        mMarkerItems = mMapMarkerItems.getMarkerItems();
        
        getSchedule();
        
        placeBottomSheet =
                new PlaceBottomSheet(this, mMarkerItems, mMainSchedule, mSubScheduleList);
        
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
        
        mViewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(this, mSubScheduleList);
        mViewPager.setAdapter(adapter);
        mViewPager.setNestedScrollingEnabled(false);
        
        
//        scheduleBottomSheet = new ScheduleBottomSheet(this, mMainSchedule, mSubScheduleList);
        NestedScrollView scheduleLayout = (NestedScrollView) findViewById(R.id.scrollView);
        mBSBSchedule = BottomSheetBehavior.from(scheduleLayout);
        
        // move to n일차
        mViewPager.setCurrentItem(position);
    }
    
    public void adapterChange(int position) {
        Log.i(TAG, "adapterchange: " + "position: " + position);
        adapter.adapter.notifyItemChanged(position);
    }
    
    public void getSchedule() {
        Intent intent = getIntent();
        mMainSchedule = intent.getParcelableExtra("MainSchedule");
        mSubScheduleList = intent.getParcelableArrayListExtra("SubScheduleList");
        position = intent.getIntExtra("position", 0);
    
        mMainSchedule.getDateBetween();
        String titleString = null;
    
        
        if (mMainSchedule != null) {
            //   System.out.println(mMainSchedule.getmFirstDate());
            titleString = mMainSchedule.getDateString();
        }
    
        if (mSubScheduleList != null) {
            SubScheduleInfo ssi = mSubScheduleList.get(0);
            //  ArrayList<String> list = ssi.getAddress();
            //   System.out.println("list" + list.get(0) + mSubScheduleList.size() + mSubScheduleList.get(0).getPlaceName());
        }
    
        mTitle.setText(titleString);
    }
    
    // click event
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                finish();
                break;
        }
    }
    
    // marker POIItem click event
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
    
    // MapView Click event
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
