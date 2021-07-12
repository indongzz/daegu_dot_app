package com.kop.daegudot.KakaoMap;

import androidx.appcompat.app.AppCompatActivity;
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
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleInfo;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.SubSchedule;
import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class MapMainActivity extends AppCompatActivity implements MapView.MapViewEventListener,
        MapView.POIItemEventListener, View.OnClickListener {
    private static final String TAG = "MapMainActivity";
    private MapView mMapView;
    private Context mContext;
    
    TextView mTitle;    // default = 장소 검색
    ImageButton mBackBtn;
    
    /* RX Schedule */
    long mMainId;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    
    
    MapMarkerItems mMapMarkerItems;     // set map markerItems
    MapUIControl mMapUIControl;         // to control category and hash tag button
    int categoryFlag = 0;
    
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
    MapPOIItem prevPOIItem = null;
    
    public ProgressBar progressBar;     // 로딩 중
    
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
        mMapMarkerItems.startRx2(128.601705,35.871344);
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
        placeName.add(mPlaceList.get(tag).attractName);
        address.add(mPlaceList.get(tag).address);
        
        mSubScheduleList.get(position).setAddress(address);
        mSubScheduleList.get(position).setPlaceName(placeName);
        
        Log.i(TAG, "adapterchange: " + "position: " + position);
        adapter.adapter.notifyItemChanged(position);
    }
    
    // TODO: 수정 필요함
    
    /** 지금은 홈에서 누르는 메인이랑, 추가로 누르는 메인이 다른데.
     * 홈에서 누르는 메인이랑 추가로 누르는 메인이 다른건 서브스케쥴 유무 차이인데.
     * 데이터베이스가 추가되면 확인 필요가 없음. 어떤 id를 불러왔느냐가 중요한거같은데
     * 그 main schedule id를 판별할 수 있는 방법을 알아내는게 중요함!
     * 아마 intent로 넘어가는 id로 알 수 있을거같음
     */
    
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
            Log.d(TAG, "mainSchedule start day: " + addStartDay + " " + addEndDay);
            mMainSchedule.setmStartDate(addStartDay);
            mMainSchedule.setmEndDate(addEndDay);
            mMainSchedule.setmDDate();
        
            mSubScheduleList = new ArrayList<>();
            for (int i = 0; i < mMainSchedule.getDateBetween(); i++) {
                SubScheduleInfo data = new SubScheduleInfo();
                LocalDate[] dateArray = mMainSchedule.getDateArray();
                String dateText = i + 1 + "일차 - "
                        + dateArray[i].format(DateTimeFormatter.ofPattern("MM.dd"));
            
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
//
//        // TODO: main id 제대로 전달할 방법 찾기.
//        mMainId = intent.getLongExtra("mainId", 0);
//        Log.d("RX MAIN ID ", "mainID: " + mMainId);
//
//        if (mMainId == 0) {
//            // 뭔가 예외 처리가 필요한가?
//            Log.d("RX LOG GETSCHEDULE", "Cannot get main id");
//        } else {
//            // TODO: get subschedule by using main id
//            Log.d("RX LOG GETSCHEDULE", "get Subschedule by main id");
//
//            getScheduleRx(mMainId);
//
//        }
        
    }
    
    private void getScheduleRx(long mainScheduleId) {
        Log.d("RX getScheduleRx", "STart!!!!!!!!!!!!");
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<List<SubSchedule>> observable = service.getSubscheduleList(mainScheduleId);
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<SubSchedule>>() {
                    @Override
                    public void onNext(List<SubSchedule> response) {
                        Log.d("RX " + TAG, "getsubschedule: " + "Next");
                        if (response.size() == 0) {
                            /* no subschedule maded need to make subschedule arraylist */
                            
                        } else {
                            Log.d("RX!!!!!!@@@@@@", "response: " + response.get(0));
                        }
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, "getsubschedule: " + e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "getsubschedule: complete");
                        
                        /* update ui after get schedule lists */
                        Log.d("RX " + TAG, "getsubschedule - " + mContext.toString());
                        /* BottomSheet */
                        placeBottomSheet =
                                new PlaceBottomSheet(mContext, mMainSchedule, mSubScheduleList);
    
                        Log.d("RX " + TAG, "getsubschedule: " + placeBottomSheet.toString());
                        
                        CoordinatorLayout placeLayout = (CoordinatorLayout) findViewById(R.id.bottomSheet);
                        mBSBPlace = BottomSheetBehavior.from(placeLayout);
                        mBSBPlace.setState(BottomSheetBehavior.STATE_HIDDEN);
    
                        mViewPager = findViewById(R.id.viewPager);
                        adapter = new ViewPagerAdapter(mContext, mSubScheduleList);
                        mViewPager.setAdapter(adapter);
                        mViewPager.setNestedScrollingEnabled(false);
    
                        NestedScrollView scheduleLayout = (NestedScrollView) findViewById(R.id.scrollView);
                        mBSBSchedule = BottomSheetBehavior.from(scheduleLayout);
    
    
                        Log.d("RX " + TAG, "getsubschedule: position: " + position);
                        // move to n일차
                        mViewPager.setCurrentItem(position);
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
