package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.kop.daegudot.Network.Place;
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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** To Set Map MarkerItems
 *
 */

public class MapMarkerItems {
    private final static String TAG = "MapMarkerItems";
    Context mContext;
    MapView mMapView;
    
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    ArrayList<Place> mPlaceList;
    ArrayList<MapPOIItem> mMarkerList;
    
    private String key = "KakaoAK " + "fede27d02cb9592216a0a526b8683677";
    
    MapMarkerItems(Context context, MapView mapView) {
        mContext = context;
        mMapView = mapView;
    
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
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
                return;
            }
            Log.e("RxJava_HOOK", "Undeliverable exception received, not sure what to do" + e.getMessage());
        });
        
    }
    
    public void setMarkerItems() {
        startRx();
    }
    
    public ArrayList<Place> getPlaceList() {
        return mPlaceList;
    }
    
    public ArrayList<MapPOIItem> getmMarkerList() {
        return mMarkerList;
    }
    
    private void startRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<List<Place>> observable = service.getPlaceList();
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Place>>() {
                            @Override
                            public void onNext(List<Place> response) {
                                Log.d("RX", "Next");
                                mPlaceList = (ArrayList<Place>) response;
                            }
                    
                            @Override
                            public void onError(Throwable e) {
                                Log.d("RX", e.getMessage());
                            }
                    
                            @Override
                            public void onComplete() {
                                Log.d("RX", "complete");
                                
                                setMarkerRx();
                                
                                /* Progress Loading done */
                                ((MapMainActivity) mContext).progressBar.setVisibility(View.GONE);
                            }
                        })
        );
    }
    
    /* get MapPoint by Address
     * set Markers on map
     */
    private void setMarkerRx() {
        RestApiService service2 = RestfulAdapter.getInstance().getKakaoServiceApi();
        
        int n = mPlaceList.size();
        mMarkerList = new ArrayList<>();
    
        for (int i = 0; i < n; i++) {
            MapPOIItem marker = new MapPOIItem();
        
            marker.setItemName(mPlaceList.get(i).getAttractName());
            marker.setTag(i);
        
            Call<Documents> data = service2.getSearchAddress(key, mPlaceList.get(i).getAddress());
            int finalI = i;
            data.enqueue(new Callback<Documents>() {
                @Override
                public void onResponse(Call<Documents> call, Response<Documents> response) {
                    Log.d("RX_ADDRESS", "code: " + response.body());
                    Documents body = response.body();
                
                    if (response.body() != null && body.getDocuments().size() != 0) {
                        Documents.Address address = body.getDocuments().get(0);
                        Log.d("RX_ADDRESSS", "address: " + address.getAddressName());
                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(
                                address.getY(), address.getX());
                        mPlaceList.get(finalI).setMapPoint(mapPoint);
                        marker.setMapPoint(mapPoint);
                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomImageResourceId(R.drawable.blue_pin);
                        marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomSelectedImageResourceId(R.drawable.big_yellow_pin);
                        marker.setShowCalloutBalloonOnTouch(false);
                        mMapView.addPOIItem(marker);
                        mMarkerList.add(marker);
                    }
                }
            
                @Override
                public void onFailure(Call<Documents> call, Throwable t) {
                    Log.d("RX_ADDRESS", "Failure!!");
                    t.printStackTrace();
                }
            });
        }
    }
}