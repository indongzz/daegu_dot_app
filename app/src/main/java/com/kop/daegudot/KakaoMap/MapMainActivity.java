package com.kop.daegudot.KakaoMap;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kop.daegudot.R;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    
        Geocoder geocoder = new Geocoder(this);
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
        
        List<Address> list = null;
        ArrayList<MapPoint.GeoCoordinate> addressList = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            try {
                list = geocoder.getFromLocationName(address[i], 20);
                System.out.println(list);
                for (Address addr : list) {
                    addressList.add(new MapPoint.GeoCoordinate(addr.getLatitude(), addr.getLongitude()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("GetLocationName", "location error");
            }
        }
        
        for (int i = 0; i< 5; i++) {
            System.out.println("latitude : " + addressList.get(i).latitude + ", longitude : " + addressList.get(i).longitude);
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(attractname[i]);
            marker.setTag(i);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(addressList.get(i).latitude, addressList.get(i).longitude));
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.blue_pin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomSelectedImageResourceId(R.drawable.yellow_pin);
    
            mMapView.addPOIItem(marker);
        }
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

class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
    private final View mCalloutBalloon;
    
    CustomCalloutBalloonAdapter(View mCalloutBalloon) {
        this.mCalloutBalloon = mCalloutBalloon;
    }
    
    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }
    
    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }
}