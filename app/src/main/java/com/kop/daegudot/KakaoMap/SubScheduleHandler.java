package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.kop.daegudot.MySchedule.DateSubSchedule;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.SubScheduleRegister;
import com.kop.daegudot.Network.Schedule.SubScheduleResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 *  서브스케줄 서버에 추가, 삭제
 */
public class SubScheduleHandler {
    private static final String TAG = "SubScheduleHandler";
    private Context mContext;
    private SharedPreferences pref;
    private String mToken;
    
    /* RX Schedule */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    
    public SubScheduleHandler(Context context) {
        mContext = context;
        
        pref = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
    }
    
    public void registerSubSchedule(SubScheduleRegister subScheduleRegister, Place place) {
        registerSubScheduleRx(subScheduleRegister, place);
    }
    
    public void deleteSubSchedule(SubScheduleResponse subScheduleResponse) {
        deleteSubScheduleRx(subScheduleResponse);
    }
    
    private void registerSubScheduleRx(SubScheduleRegister subScheduleRegister, Place place) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<Long> observable = service.registerSubschdule(subScheduleRegister);
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.d("RX " + TAG, "register subschedule: " + "Next");
                        Toast.makeText(mContext, "추가되었습니다", Toast.LENGTH_SHORT).show();
                        SubScheduleResponse subScheduleResponse = new SubScheduleResponse();
                        subScheduleResponse.id = response;
                        subScheduleResponse.date = subScheduleRegister.date;
                        subScheduleResponse.placesResponseDto = place;
    
                        ArrayList<DateSubSchedule> dateSubList = ((MapMainActivity) mContext).mDateSubScheduleList;
                        int n = dateSubList.size();
                        
                        for (int i = 0; i < n; i++) {
                            if ((subScheduleResponse.date).equals(dateSubList.get(i).date)) {
                                ((MapMainActivity)mContext).mDateSubScheduleList.get(i)
                                        .subScheduleList.add(subScheduleResponse);
                                break;
                            }
                        }
                        
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, "register subschedule: " + e.getMessage());
                        Toast.makeText(mContext, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "register subschedule: complete");
                        ((MapMainActivity) mContext).mMainScheduleBottomSheetAdapter
                                .mSubScheduleBottomSheetAdapter.notifyDataSetChanged();
                        ((MapMainActivity) mContext).updateBottomSheetUI();
                    }
                })
        );
    }
    
    private void deleteSubScheduleRx(SubScheduleResponse subScheduleResponse) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<Long> observable = service.deleteSubSchedule(subScheduleResponse.id);
    
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.d("RX " + TAG, "delete subschedule: " + "Next");
                        if (response == 0L) {
                            Log.d("RX " + TAG, "delete subSchedule failed");
                        } else if (response == 1L) {
                            Log.d("RX " + TAG, "delete subSchedule success");
                            Toast.makeText(mContext, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, "delete subschedule: " + e.getMessage());
                    }
                
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "delete subschedule: complete");
                    }
                })
        );
    }
}
