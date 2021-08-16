package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/** To Set Map MarkerItems
 *  TR 관광
 *  FD 음식
 *  AC 숙소
 */

public class MapMarkerItems {
    private final static String TAG = "MapMarkerItems";
    Context mContext;
    MapView mMapView;
    
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    ArrayList<MapPOIItem> mMarkerList;
    int index = 0;
    
    MapMarkerItems(Context context, MapView mapView) {
        mContext = context;
        mMapView = mapView;
        mMarkerList = new ArrayList<>();
        
        /* RxJava Error 난 경우 멈추지 않고 계속하기 */
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IOException) || (e instanceof SocketException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                // that's likely a bug in the application
                Objects.requireNonNull(Thread.currentThread().getUncaughtExceptionHandler())
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Objects.requireNonNull(Thread.currentThread().getUncaughtExceptionHandler())
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            assert e != null;
            Log.e("RxJava_HOOK", "Undeliverable exception received, not sure what to do" + e.getMessage());
        });
        
    }
    
    public void setMarkerItems() {
        selectAllPlaceListRx();
    }
    
    public ArrayList<MapPOIItem> getMarkerList() {
        return mMarkerList;
    }
    
    private void selectAllPlaceListRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<List<Place>> observable = service.getPlaceList();
    
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Place>>() {
                    @Override
                    public void onNext(@NonNull List<Place> response) {
                        Log.d("RX " + TAG, "Next");
                        ((MapMainActivity) mContext).mPlaceList = (ArrayList<Place>) response;
                    }
                
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("RX " + TAG, Objects.requireNonNull(e.getMessage()));
                    }
                
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");
                    
                        setServerMarker();
                    }
                })
        );
    }
    
    /* get MapPoint by Address
     * set Markers on map
     */
    private void setServerMarker() {
        for (Place place : ((MapMainActivity) mContext).mPlaceList) {
            ((MapMainActivity) mContext).mPlaceList.get(index).tag =
                    (int) ((MapMainActivity) mContext).mPlaceList.get(index).id;
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(place.attractName);
            marker.setTag(((MapMainActivity) mContext).mPlaceList.get(index).tag);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(
                    Double.parseDouble(place.latitude), Double.parseDouble(place.longitude));
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.blue_pin2);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomSelectedImageResourceId(R.drawable.big_yellow_pin);
            marker.setShowCalloutBalloonOnTouch(false);
            mMarkerList.add(marker);
            index++;
        }
        
        ((MapMainActivity) mContext).addPOItoMapView();
    
        /* if move from recommend list or wishlist activity*/
        ((MapMainActivity) mContext).notifyPlaceListDone();
    }
}