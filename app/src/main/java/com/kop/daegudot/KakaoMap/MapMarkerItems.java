package com.kop.daegudot.KakaoMap;

import android.content.Context;

import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

/** To Set Map MarkerItems
 *
 */

public class MapMarkerItems {
    Context mContext;
    ArrayList<MarkerInfo> mMarkerItems;
    MapView mMapView;
    
    
    MapMarkerItems(Context context, MapView mapView) {
        mContext = context;
        mMapView = mapView;
    }
    
    public void setMarkerItems() {
        // TODO: marker로 표시할 data 받아서 markerinfo에 담기
    
        /* example Marker Data */
        String[] address = {
                "대구광역시 중구 남산로 4길 112",
                "대구광역시 중구 서성로 6-1",
                "대구광역시 동구동화사 1길 1",
                "대구광역시 중구 국채보상로 670",
                "대구광역시 중구 동성로 2길 80"
        };
        String[] attractname = {"성모당", "서상돈 고택", "동화사 보사계 유공비", "국채보상운동기념공원", "2.28기념중앙공원"};
        String[] tel = {
                "053-250-3000", "053-256-3762",
                "53-982-0101-2", "053-254-9401",
                "053-254-9405"
        };
    
        mMarkerItems = new ArrayList<>();
    
        for (int i = 0; i < 5; i++) {
            MarkerInfo markerItem = new MarkerInfo(mContext);
            markerItem.setAddress(address[i]);
            markerItem.setName(attractname[i]);
            markerItem.setTel(tel[i]);
            markerItem.setRate((float) (i + 0.5));
            markerItem.setLike(false);
            mMarkerItems.add(markerItem);
        }
    
        for (int i = 0; i < 5; i++) {
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
    }
    
    public ArrayList<MarkerInfo> getMarkerItems() {
        return mMarkerItems;
    }
}
