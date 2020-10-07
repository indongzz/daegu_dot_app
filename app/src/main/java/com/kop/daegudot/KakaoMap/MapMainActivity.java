package com.kop.daegudot.KakaoMap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapMainActivity extends AppCompatActivity implements MapView.POIItemEventListener {
    private static final String LOG_TAG = "MapMainActivity";
    private MapView mMapView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        
        mMapView = findViewById(R.id.map_view);
    
        mMapView.setPOIItemEventListener(this);
        mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.871344, 128.601705), true);
        mMapView.setZoomLevel(6, true);
    
        Intent intent = getIntent();
        if (intent != null) {
            String startDay = intent.getStringExtra("startDay");
            String endDay = intent.getStringExtra("endDay");
            
            System.out.println(startDay + "--------" + endDay);
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
}