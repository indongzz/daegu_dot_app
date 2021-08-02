package com.kop.daegudot.KakaoMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.SubScheduleRegister;
import com.kop.daegudot.Network.Schedule.SubScheduleResponse;
import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MapMainActivity extends AppCompatActivity implements MapView.MapViewEventListener,
        MapView.POIItemEventListener, View.OnClickListener {
    private static final String TAG = "MapMainActivity";
    private MapView mMapView;
    private Context mContext;
    
    TextView mTitle;    // default = 장소 검색
    ImageButton mBackBtn;
    
    /* RX Schedule */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    
    MapMarkerItems mMapMarkerItems;     // set map markerItems
    MapUIControl mMapUIControl;         // to control category and hash tag button
    int categoryFlag = 0;
    
    // get Main and sub schedule list from previous activity
    MainScheduleInfo mMainSchedule;
    ArrayList<DateSubSchedule> mDateSubScheduleList;
    int position = 0; // default = first page
    Place mPlace;
    
    // RX PoiItems
    ArrayList<Place> mPlaceList;
    private BottomSheetBehavior mBSBPlace;
    PlaceBottomSheet placeBottomSheet;
    private BottomSheetBehavior mBSBSchedule;
    ViewPager2 mMainListView;
    MainScheduleBottomSheetAdapter mMainScheduleBottomSheetAdapter;
    MapPOIItem prevPOIItem = null;
    private SharedPreferences pref;
    private String mToken;
    
    public ProgressBar progressBar;     // 로딩 중
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        mContext = MapMainActivity.this;
    
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        
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
        mMapView.setZoomLevel(3, true);
        
        // Set Hash Tag button and Category button
        mMapUIControl = new MapUIControl(this, view);
        mMapUIControl.setCategoryBtn();
        
        // Set MarkerItems
        mMapMarkerItems = new MapMarkerItems(this, mMapView);
        mMapMarkerItems.setMarkerItems();
        mMapMarkerItems.selectAllKakaoPlaceListRx(128.601705,35.871344);
        mPlaceList = updatePlaceList();
        
        getSchedule();
        
        /* BottomSheet */
        placeBottomSheet =
                new PlaceBottomSheet(this, mMainSchedule, mDateSubScheduleList);

        CoordinatorLayout placeLayout = (CoordinatorLayout) findViewById(R.id.bottomSheet);
        mBSBPlace = BottomSheetBehavior.from(placeLayout);
        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);

        mMainListView = findViewById(R.id.viewPager);

        NestedScrollView scheduleLayout = (NestedScrollView) findViewById(R.id.scrollView);
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
    
        CoordinatorLayout placeLayout = (CoordinatorLayout) findViewById(R.id.bottomSheet);
        mBSBPlace = BottomSheetBehavior.from(placeLayout);
        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);
    
        mMainListView = findViewById(R.id.viewPager);
        mMainScheduleBottomSheetAdapter = new MainScheduleBottomSheetAdapter(mContext, mMainSchedule, mDateSubScheduleList);
        mMainListView.setAdapter(mMainScheduleBottomSheetAdapter);
        mMainListView.setNestedScrollingEnabled(false);
        mMainListView.setOffscreenPageLimit(mMainSchedule.getDateBetween() * 2);
    
        NestedScrollView scheduleLayout = (NestedScrollView) findViewById(R.id.scrollView);
        mBSBSchedule = BottomSheetBehavior.from(scheduleLayout);
        
        // move to n일차
        mMainListView.setCurrentItem(position);
    }
    
    public void adapterChange(String date, int tag) {
        /* update Place list */
        mPlaceList = updatePlaceList();
        
        Log.d("RX " + TAG, "adapter change: " + mPlaceList.get(tag).id + " " + tag);
        SubScheduleRegister subScheduleRegister = new SubScheduleRegister();
        subScheduleRegister.mainScheduleId = mMainSchedule.getMainId();
        subScheduleRegister.date = date;
        subScheduleRegister.placesId = mPlaceList.get(tag).id;
        
        registerSubSchedule(subScheduleRegister, tag);
    }
    
    private void registerSubSchedule(SubScheduleRegister subScheduleRegister, int tag) {
        Log.d("RX getScheduleRx", "register!!!!" + mMainSchedule.getMainId());
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<Long> observable = service.registerSubschdule(subScheduleRegister);
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.d("RX " + TAG, "register subschedule: " + "Next");
    
    
                        SubScheduleResponse subScheduleResponse = new SubScheduleResponse();
                        subScheduleResponse.id = response;
                        subScheduleResponse.date = subScheduleRegister.date;
                        subScheduleResponse.placesResponseDto = mPlaceList.get(tag);
    
                        for (int i = 0; i < mDateSubScheduleList.size(); i++) {
                            if ((subScheduleResponse.date)
                                    .equals(mDateSubScheduleList.get(i).date)) {
                                mDateSubScheduleList.get(i)
                                        .subScheduleList.add(subScheduleResponse);
                                break;
                            }
                        }
    
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, "register subschedule: " + e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "register subschedule: complete");
                        mMainScheduleBottomSheetAdapter.mSubScheduleBottomSheetAdapter.notify();
                    }
                })
        );
    }
    
    public ArrayList<Place> updatePlaceList() {
        return mMapMarkerItems.getPlaceList();
    }
    
    // click event
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            finish();
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
        prevPOIItem = null;
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
        
        addPOItoMapView(lists);
    }
    
    public void changeCategory(int category) {
        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBSBSchedule.setState(BottomSheetBehavior.STATE_EXPANDED);
        prevPOIItem = null;
        categoryFlag = category;
        
        ArrayList<MapPOIItem> lists = mMapMarkerItems.getmMarkerList();
        addPOItoMapView(lists);
    }
    
    public void addPOItoMapView(ArrayList<MapPOIItem> lists) {
        mMapView.removeAllPOIItems();
        
        for (MapPOIItem item: lists) {
            if (mMapView.getMapPointBounds().contains(item.getMapPoint())) {
                if (checkCategory(item)) {
                    mMapView.addPOIItem(item);
                }
            }
        }
        
        // 이전에 클릭한 POIItem 띄우기
        if (prevPOIItem != null) {
            mMapView.selectPOIItem(prevPOIItem, true);
        }
    }
    
    public boolean checkCategory(MapPOIItem item) {
        boolean bool = false;
        String category = mPlaceList.get(item.getTag()).category;
        
        if (category == null) {
            if (categoryFlag == 0) {
                bool = true;
            }
        } else {
            if (categoryFlag == 1 && category.equals("AD5")) {  // 숙박
                bool = true;
            } else if (categoryFlag == 2 && category.equals("FD6")) {  // 음식
                bool = true;
            } else if (categoryFlag == 3 && category.equals("CE7")) {  // 카페
                bool = true;
            }
        }
        
        return bool;
    }
}
