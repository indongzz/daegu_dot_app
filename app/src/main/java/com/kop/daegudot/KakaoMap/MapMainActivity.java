package com.kop.daegudot.KakaoMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kop.daegudot.MySchedule.DateSubSchedule;
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleDialog;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapMainActivity extends AppCompatActivity implements MapView.MapViewEventListener,
        MapView.POIItemEventListener, View.OnClickListener {
    private static final String TAG = "MapMainActivity";
    private MapView mMapView;
    private Context mContext;
    
    TextView mTitle;    // default = 장소 검색
    ImageButton mBackBtn;
    SearchView mSearchView;
    public ProgressBar progressBar;     // 로딩 중
    
    MapMarkerItems mMapMarkerItems;     // set map markerItems
    MapUIControl mMapUIControl;         // to control category and hash tag button
    
    // get Main and sub schedule list from previous activity
    MainScheduleInfo mMainSchedule;
    ArrayList<DateSubSchedule> mDateSubScheduleList;
    int position = 0; // default = first page
    Place mPlace;
    
    ArrayList<Place> mPlaceList = new ArrayList<>();
    BottomSheetBehavior<View> mBSBPlace;
    PlaceBottomSheet placeBottomSheet;
    BottomSheetBehavior<View> mBSBSchedule;
    ViewPager2 mMainListView;
    MainScheduleBottomSheetAdapter mMainScheduleBottomSheetAdapter;
    MapPOIItem prevPOIItem = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        mContext = MapMainActivity.this;
        
        View view = getWindow().getDecorView();
        
        mTitle = findViewById(R.id.title);
        mTitle.setText("장소 검색");
    
        mBackBtn = findViewById(R.id.backBtn);
        mBackBtn.setOnClickListener(this);
        
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);
        SearchViewHandler searchViewHandler = new SearchViewHandler(mContext);
        searchViewHandler.setSearchView();
        
        progressBar = findViewById(R.id.progress_bar);
        
        /* Map View */
        mMapView = findViewById(R.id.map_view);
        
        mMapView.setPOIItemEventListener(this);
        mMapView.setMapViewEventListener(this);
        mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.871344, 128.601705), true);
        mMapView.setZoomLevel(3, true);
        
        // Set Hash Tag button and Category button
        mMapUIControl = new MapUIControl(this, view);
        mMapUIControl.setCategoryBtn();
        
        // Set MarkerItems
        mMapMarkerItems = new MapMarkerItems(this, mMapView);
        mMapMarkerItems.setMarkerItems();
//        mPlaceList = updatePlaceList();
        
        getSchedule();

        /* BottomSheet */
        placeBottomSheet =
                new PlaceBottomSheet(this, mMainSchedule, mDateSubScheduleList);

        CoordinatorLayout placeLayout = findViewById(R.id.bottomSheet);
        mBSBPlace = BottomSheetBehavior.from(placeLayout);
        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);

        mMainListView = findViewById(R.id.viewPager);
        mMainListView.setNestedScrollingEnabled(false);

        NestedScrollView scheduleLayout = findViewById(R.id.scrollView);
        mBSBSchedule = BottomSheetBehavior.from(scheduleLayout);
    }
    
    /* MainSchedule 이용해서 SubSchedule List 서버로부터 받아오기 */
    public void getSchedule() {
        Intent intent = getIntent();
        
        // MyScheduleFragment, AddScheduleFragment 둘 다 MainSchedule 보내옴
        // position은 MyScheduleFragment에서 SubSchedule 2일차를 누르면 2번째 BottomSheet을 띄움
        mMainSchedule = intent.getParcelableExtra("mainSchedule");
        position = intent.getIntExtra("position", 0);
        // 추천글에서 일정 Chip 클릭 시
        mPlace = intent.getParcelableExtra("markerPlace");
        
        if (mMainSchedule != null) {
            String titleString = mMainSchedule.getDateString();
            mTitle.setText(titleString);
    
            mDateSubScheduleList = intent.getParcelableArrayListExtra("dateSubSchedule");
            if (mDateSubScheduleList == null) {
                mDateSubScheduleList = SubScheduleDialog.getDateSubSchedules(mMainSchedule, null);
            }
            updateBottomSheetUI();
        }
    }
    
    public void notifyPlaceListDone(ArrayList<Place> placeList) {
        mPlaceList = placeList;
        if (mPlace != null) {
            placeBottomSheet.changePlaceBottomSheet((int) mPlace.id);
            mBSBPlace.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
    
    public void updateBottomSheetUI() {
        /* BottomSheet */
        placeBottomSheet =
                new PlaceBottomSheet(mContext, mMainSchedule, mDateSubScheduleList);
    
        CoordinatorLayout placeLayout = findViewById(R.id.bottomSheet);
        mBSBPlace = BottomSheetBehavior.from(placeLayout);
        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);
    
        mMainListView = findViewById(R.id.viewPager);
        mMainScheduleBottomSheetAdapter = new MainScheduleBottomSheetAdapter(mContext, mMainSchedule, mDateSubScheduleList);
        mMainListView.setAdapter(mMainScheduleBottomSheetAdapter);
        mMainListView.setOffscreenPageLimit(mMainSchedule.getDateBetween() * 2);
    
        NestedScrollView scheduleLayout = findViewById(R.id.scrollView);
        mBSBSchedule = BottomSheetBehavior.from(scheduleLayout);
        
        // move to n일차
        mMainListView.setCurrentItem(position);
    }
    
    // click event
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            finish();
        }
        if (v.getId() == R.id.search_view) {
            mSearchView.onActionViewExpanded();
        }
    }
    
    // marker POIItem click event
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        placeBottomSheet.changePlaceBottomSheet(mapPOIItem.getTag());
        mBSBPlace.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBSBSchedule.setState(BottomSheetBehavior.STATE_HIDDEN);
        prevPOIItem = mapPOIItem;
        Log.d(TAG, "map: " + mapPOIItem.getItemName() + " " + mapPOIItem.getTag());
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
        Log.d(TAG, "MapView Initialized");
    }
    
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "MapView Center Point Moved");
    }
    
    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        Log.d(TAG, "MapView Zoom Level Changed");
    }
    
    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "MapView Single Tapped");
        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBSBSchedule.setState(BottomSheetBehavior.STATE_EXPANDED);
        prevPOIItem = null;
        
        mSearchView.onActionViewCollapsed();
    }
    
    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "MapView Double Tapped");
    }
    
    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "MapView Long Pressed");
    }
    
    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "MapView Drag Started");
    }
    
    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "MapView Drag Ended");
    }
    
    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        Log.d(TAG, "MapView Moved: " + mPlaceList.size());
        
        addPOItoMapView();
    }
    
    public void addPOItoMapView() {
        ArrayList<MapPOIItem> lists = mMapMarkerItems.getMarkerList();
        mMapView.removeAllPOIItems();
        
        int count = 0;
        for (MapPOIItem item: lists) {
            if (mMapView.getMapPointBounds().contains(item.getMapPoint())) {
                if (mMapUIControl.checkCategory(item)) {
                    mMapView.addPOIItem(item);
                    count++;
                }
            }
            if (count > 100) break;
        }
        
        // 이전에 클릭한 POIItem 띄우기
        if (prevPOIItem != null) {
            mMapView.selectPOIItem(prevPOIItem, true);
        }
    }
}
