package com.kop.daegudot.Recommend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kop.daegudot.Network.Recommend.RecommendRegister;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.MainScheduleResponse;
import com.kop.daegudot.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class UpdateRecommendSchedule {
    private final String TAG = "UpdateRecommendSchedule";
    Context mContext;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    RecommendResponse mRecommendPost;
    private String mToken;
    
    public UpdateRecommendSchedule(Context context, String token) {
        mContext = context;
        mToken = token;
        
        getPreviousContent();
    }
    
    public void getPreviousContent() {
        Intent intent = ((AddRecommendActivity)mContext).getIntent();
        if (intent != null) {
            mRecommendPost = intent.getParcelableExtra("recommendPost");
            if (mRecommendPost != null) {
                MainScheduleResponse main = mRecommendPost.mainScheduleResponseDto;
                String date = main.startDate.substring(5, 7) + "." + main.startDate.substring(8, 10)+ " ~ " +
                        main.endDate.substring(5, 7) + "." +  main.endDate.substring(8, 10);
                
                ((AddRecommendActivity)mContext).setViewUI(date, mRecommendPost.hashtags,
                        (float)mRecommendPost.star, mRecommendPost.title, mRecommendPost.content);
            }
        }
    }
    
    void updateRecommendScheduleRx(RecommendRegister recommendRegister) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<Long> observable = service.updateRecommendSchedule(mRecommendPost.id, recommendRegister);
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.d("RX " + TAG, "Next");
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");
                        // TODO: update recycler views
                        RecommendListActivity.refresh();
                    }
                })
        );
    }
}
