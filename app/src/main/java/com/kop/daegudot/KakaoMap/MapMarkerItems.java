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
import java.lang.reflect.Array;
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
 *    AD5	숙박
 *    FD6	음식점
 *    CE7	카페
 */

public class MapMarkerItems {
    private final static String TAG = "MapMarkerItems";
    Context mContext;
    MapView mMapView;
    
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    ArrayList<Place> mPlaceList;
    ArrayList<MapPOIItem> mMarkerList;
    ArrayList<Documents.Address> mAccomdList, mFoodList, mCafeList;
    
    private String key = "KakaoAK " + "fede27d02cb9592216a0a526b8683677";
    
    MapMarkerItems(Context context, MapView mapView) {
        mContext = context;
        mMapView = mapView;
        mMarkerList = new ArrayList<>();
        mPlaceList = new ArrayList<>();
        mAccomdList = new ArrayList<>();
        mFoodList = new ArrayList<>();
        mCafeList = new ArrayList<>();
        
    
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
        
        for (int i = 0; i < n; i++) {
            MapPOIItem marker = new MapPOIItem();
            
            marker.setItemName(mPlaceList.get(i).attractName);
            marker.setTag(i);
            
            Call<Documents> data = service2.getSearchAddress(key, mPlaceList.get(i).address);
            int finalI = i;
            data.enqueue(new Callback<Documents>() {
                @Override
                public void onResponse(Call<Documents> call, Response<Documents> response) {
//                    Log.d("RX_ADDRESS", "code: " + response.body());
                    Documents body = response.body();
                    
                    if (response.body() != null && body.getDocuments().size() != 0) {
                        Documents.Address address = body.getDocuments().get(0);
//                        Log.d("RX_ADDRESSS", "address: " + address.getAddressName());
                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(
                                address.getY(), address.getX());
                        mPlaceList.get(finalI).mapPoint = mapPoint;
                        marker.setMapPoint(mapPoint);
                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomImageResourceId(R.drawable.blue_pin);
                        marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomSelectedImageResourceId(R.drawable.big_yellow_pin);
                        marker.setShowCalloutBalloonOnTouch(false);
                        mMapView.addPOIItem(marker);
                        mMarkerList.add(marker);
                    } else {
                        Log.d("RX_ADDRESS", mPlaceList.get(finalI).address);
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
    
    // Get Place by Category
    // by using Kakao API
    //AD5	숙박
    //FD6	음식점
    //CE7	카페
    public void startRx2(double x, double y) {
        Log.d(TAG, "x: " + x + "  y: " + y);
        
        RestApiService service = RestfulAdapter.getInstance().getKakaoServiceApi();
        Call<Documents> call = service.getPlacebyCategory(
                key, "AD5", x + "",y + "", 10000);
        
        Log.d(TAG, "call getPlace by Category");
        call.enqueue(new Callback<Documents>() {
            @Override
            public void onResponse(Call<Documents> call, Response<Documents> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d(TAG, response.body().getDocuments().toString());
                    mAccomdList.addAll(response.body().getDocuments());
                    setPlaceMarker(mAccomdList);
                    
                    call = service.getPlacebyCategory(
                            key, "FD6", x + "", y + "", 10000);
                    call.enqueue(new Callback<Documents>() {
                        @Override
                        public void onResponse(Call<Documents> call, Response<Documents> response) {
                            if (response.isSuccessful()) {
                                mFoodList.addAll(response.body().getDocuments());
    
                                setPlaceMarker(mFoodList);
                            } // FD if문 종료
                        }
    
                        @Override
                        public void onFailure(Call<Documents> call, Throwable t) {
                            Log.d(TAG, "FD6. Failure Code");
                        }
                    });
                } else {
                    Log.d(TAG, "code: " + response.code());
                }   // AD if문 종료
                
            }
            
            @Override
            public void onFailure(Call<Documents> call, Throwable t) {
            
            }
        });
    
        Call<Documents> cafeCall = service.getPlacebyCategory(
                key, "CE7", x + "", y + "", 10000);
        cafeCall.enqueue(new Callback<Documents>() {
            @Override
            public void onResponse(Call<Documents> call, Response<Documents> response) {
                if (response.isSuccessful()) {
                    mCafeList.addAll(response.body().getDocuments());
                    setPlaceMarker(mCafeList);
                } else {
                    Log.d(TAG, "Cafe response error code: " + response.code());
                }
            }
        
            @Override
            public void onFailure(Call<Documents> call, Throwable t) {
                Log.d(TAG, "Cafe Failure");
            }
        });
    }
    
    // set Markers from kakao
    public void setPlaceMarker(ArrayList<Documents.Address> arrayList) {
        int tagNum = 0;
        for (Documents.Address address : arrayList) {
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(address.getAddressName());
            marker.setTag(tagNum++);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(address.getY(), address.getX());
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.blue_pin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomSelectedImageResourceId(R.drawable.big_yellow_pin);
            marker.setShowCalloutBalloonOnTouch(false);
            mMapView.addPOIItem(marker);
            mMarkerList.add(marker);
        
            Place place = new Place();
            place.mapPoint = mapPoint;
            place.like = true;
            place.address = address.getAddress();
            place.attractName = address.getAddressName();
        
            mPlaceList.add(place);
        
            /* Progress Loading done */
            ((MapMainActivity) mContext).progressBar.setVisibility(View.GONE);
        }
    }
}