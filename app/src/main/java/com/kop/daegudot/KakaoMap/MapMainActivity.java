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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleInfo;
import com.kop.daegudot.Network.Place;
import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    
    // RX PoiItems
    ArrayList<Place> mPlaceList;
    private BottomSheetBehavior mBSBPlace;
    PlaceBottomSheet placeBottomSheet;
    private BottomSheetBehavior mBSBSchedule;
    ViewPager2 mViewPager;
    ViewPagerAdapter adapter;
    
    public ProgressBar progressBar;     // 로딩 중
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        
        View view = getWindow().getDecorView();
        
        mTitle = findViewById(R.id.title);
        mTitle.setText("장소 검색");
    
        mBackBtn = findViewById(R.id.backBtn);
        mBackBtn.setOnClickListener(this);
        
        progressBar = findViewById(R.id.progress_bar);
        
        /* Map View */
        mMapView = findViewById(R.id.map_view);
        
        mMapView.setPOIItemEventListener(this);
        mMapView.setMapViewEventListener(this);
        mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.871344, 128.601705), true);
        mMapView.setZoomLevel(4, true);
        
        // Set Hash Tag button and Category button
        mMapUIControl = new MapUIControl(this, view);
        mMapUIControl.setCategoryBtn();
        mMapUIControl.setHashBtn();
        
        // Set MarkerItems
        mMapMarkerItems = new MapMarkerItems(this, mMapView);
        mMapMarkerItems.setMarkerItems();
//        mMarkerItems = mMapMarkerItems.getMarkerItems();
        mPlaceList = updatePlaceList();
        
        // TODO: get Schedule from DB or add Schedule
        getSchedule();
        
        /* BottomSheet */
        placeBottomSheet =
                new PlaceBottomSheet(this, mMainSchedule, mSubScheduleList);
        
        CoordinatorLayout placeLayout = (CoordinatorLayout) findViewById(R.id.bottomSheet);
        mBSBPlace = BottomSheetBehavior.from(placeLayout);
        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);
        
        mViewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(this, mSubScheduleList);
        mViewPager.setAdapter(adapter);
        mViewPager.setNestedScrollingEnabled(false);
        
        NestedScrollView scheduleLayout = (NestedScrollView) findViewById(R.id.scrollView);
        mBSBSchedule = BottomSheetBehavior.from(scheduleLayout);
        
        // move to n일차
        mViewPager.setCurrentItem(position);
        
    }
    
    public void adapterChange(int position, int tag) {
        /* update Place list */
        mPlaceList = updatePlaceList();
        ArrayList<String> placeName;
        ArrayList<String> address;
        
        placeName = mSubScheduleList.get(position).getPlaceName();
        address = mSubScheduleList.get(position).getAddress();
        placeName.add(mPlaceList.get(tag).getAttractName());
        address.add(mPlaceList.get(tag).getAddress());
        
        mSubScheduleList.get(position).setAddress(address);
        mSubScheduleList.get(position).setPlaceName(placeName);
        
        Log.i(TAG, "adapterchange: " + "position: " + position);
        adapter.adapter.notifyItemChanged(position);
    }
    
    public void getSchedule() {
        Intent intent = getIntent();
        // 홈에서 mainschedule 눌러서 접근 시
        mMainSchedule = intent.getParcelableExtra("MainSchedule");
        mSubScheduleList = intent.getParcelableArrayListExtra("SubScheduleList");
        position = intent.getIntExtra("position", 0);
        // 일정 추가 할 때
        String addStartDay = intent.getStringExtra("startDay");
        String addEndDay = intent.getStringExtra("endDay");
        
        
        if (mMainSchedule != null) {
            Log.i(TAG, "mMainSchedule: " + mMainSchedule);
            mMainSchedule.getDateBetween();
        } else {
            Log.i(TAG, "mMainSchedule is null add new MainSchedule");
            mMainSchedule = new MainScheduleInfo();
            mMainSchedule.setmFirstDate(addStartDay);
            mMainSchedule.setmLastDate(addEndDay);
            mMainSchedule.setmDDate();
            
            mSubScheduleList = new ArrayList<>();
            for (int i = 0; i < mMainSchedule.getDateBetween(); i++) {
                SubScheduleInfo data = new SubScheduleInfo();
                LocalDate[] dateArray = mMainSchedule.getDateArray();
                String dateText = i + 1 + "일차 - "
                        + dateArray[i].format(DateTimeFormatter.ofPattern("MM/dd"));
                
                data.setDate(dateText);
                
                ArrayList<String> address = new ArrayList<>();
                ArrayList<String> attract = new ArrayList<>();
                
                data.setAddress(address);
                data.setPlaceName(attract);
                
                mSubScheduleList.add(data);
            }
        }
        
        String titleString = mMainSchedule.getDateString();
        
        mTitle.setText(titleString);
    }
    
    public ArrayList<Place> updatePlaceList() {
        return mMapMarkerItems.getPlaceList();
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
        ArrayList<MapPOIItem> lists = mMapMarkerItems.getmMarkerList();

        mapView.removeAllPOIItems();
        
        for (MapPOIItem item: lists) {
            if (mapView.getMapPointBounds().contains(item.getMapPoint())) {
                mapView.addPOIItem(item);
            }
        }
    }
}
