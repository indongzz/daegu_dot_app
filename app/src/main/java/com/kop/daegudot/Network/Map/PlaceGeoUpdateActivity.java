package com.kop.daegudot.Network.Map;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.kop.daegudot.KakaoMap.Documents;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceGeoUpdateActivity extends AppCompatActivity {
    
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    ArrayList<Place> mPlaceList;
    ArrayList<PlaceGeo> mGeoList;
    
    private String key = "KakaoAK " + "fede27d02cb9592216a0a526b8683677";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_geo_update);

        mGeoList = new ArrayList<>();
        
        Log.d("Placegeo update", "place!!");
        
        startRx();
    
//        new Handler().postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                int n = mGeoList.size();
//                Log.d("RX_PRINT!!", "total should be: " + mPlaceList.size() +
//                         "  geo: " + n);
//
//                for (int i = 0; i < n; i++) {
//                    Log.d("RX_PRINT", "id: " + mGeoList.get(i).id);
//                    Log.d("RX_PRINT2", "lat long: " + mGeoList.get(i).latitude +
//                            ", " + mGeoList.get(i).longitude);
//                }
//
//                putGeoRx();
//            }
//        }, 5000);   // 5초 뒤 실행
    }

    private void putGeoRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<Long> observable = service.updateLocation(mGeoList);
    
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
    
                    @Override
                    public void onNext(Long aLong) {
                        Log.d("RX_return", "return: " + aLong);
                    }
    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX_resturn", "error");
                        e.printStackTrace();
                    }
    
                    @Override
                    public void onComplete() {
                        Log.d("RX_resturn", "Complete");
                    }
                })
        );
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
            Call<Documents> data = service2.getSearchAddress(key, mPlaceList.get(i).address);
            int finalI = i;
//            Log.d("RX_ADDRESS", "!!!address:" + mPlaceList.get(i).address);
            data.enqueue(new Callback<Documents>() {
                @Override
                public void onResponse(Call<Documents> call, Response<Documents> response) {
//                    Log.d("RX_ADDRESS", "code: " + response.code());
                    Documents body = response.body();

                    if (response.body() != null && body.getDocuments().size() != 0) {
                        Documents.Address address = body.getDocuments().get(0);
//                        Log.d("RX_ADDRESSS", "address: " + address.address);
                        //                        Log.d("RX_ADDRESS", "Map: " + address.y + " , " + address.x);
//                        mPlaceList.get(finalI).mapPoint =
//                                MapPoint.mapPointWithGeoCoord(address.y, address.x);
                                
                        PlaceGeo place = new PlaceGeo();
                        place.id = mPlaceList.get(finalI).id;
//                        place.latitude = (float) address.y;
//                        place.longitude = (float) address.x;
                        place.latitude = String.valueOf(address.y);
                        place.longitude = String.valueOf(address.x);
                        mGeoList.add(place);
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

}