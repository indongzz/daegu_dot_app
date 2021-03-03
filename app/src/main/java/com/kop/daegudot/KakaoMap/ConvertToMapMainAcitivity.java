//package com.kop.daegudot.KakaoMap;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import com.kop.daegudot.MySchedule.MainScheduleInfo;
//import com.kop.daegudot.MySchedule.SubScheduleInfo;
//import com.kop.daegudot.Network.Place;
//import com.kop.daegudot.Network.RestApiService;
//import com.kop.daegudot.Network.RestfulAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.observers.DisposableObserver;
//import io.reactivex.schedulers.Schedulers;
//
//public class ConvertToMapMainAcitivity {
//    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();
//
//    public static void convertToMap(Context context) {
//
//        Intent intent = new Intent(context, MapMainActivity.class);
//        context.startActivity(intent);
//    }
//
//    public static void convertToMapWSchedule(Context context, MainScheduleInfo mainSchedule,
//                                             ArrayList<SubScheduleInfo> subScheduleList, int pos) {
//        Intent intent = new Intent(context, MapMainActivity.class);
//        intent.putExtra("MainSchedule", mainSchedule);
//        intent.putParcelableArrayListExtra("SubScheduleList", subScheduleList);
//        intent.putExtra("position", pos);
//        context.startActivity(intent);
//    }
//
//    public void convertToMapwMarker(Context context, String name) {
//
//    }
//
//    private void startRx(int m) {
//        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
//        Observable<List<Place>> observable = service.getPlaceList();
//
//        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<List<MarkerInfo>>() {
//                    @Override
//                    public void onNext(List<MarkerInfo> response) {
//                        int n = response.size();
//                        Log.d("RX", n + "개");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d("RX", e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d("RX", "complete");
//                     //   updateUI(m);
//                    }
//                })
//        );
//    }
//
//}
