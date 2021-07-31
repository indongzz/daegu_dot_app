package com.kop.daegudot.Recommend;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.kop.daegudot.MorePage.MyReview.MyCommentActivity;
import com.kop.daegudot.MorePage.MyReview.MyReviewStoryActivity;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Delete Recommend Schedule
 * 추천 글 삭제
 */
public class DeleteRecommendSchedule {
    Context mContext;
    RecommendResponse mRecommendPost;
    int position;
    
    /* Rx java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mToken;
    
    public DeleteRecommendSchedule(Context context, RecommendResponse recommendPost, int position) {
        mContext = context;
        mRecommendPost = recommendPost;
        this.position = position;
        
        SharedPreferences pref = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        
        deleteRecommendScheduleRx();
    }
    
    private void deleteRecommendScheduleRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<Long> observable = service.deleteRecommendSchedule(mRecommendPost.id);
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.d("RX", "Next");
                        
                        Toast.makeText(mContext, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                        notifyDeleted();
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX", e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX", "complete");
                        
                    }
                })
        );
    }
    
    public void notifyDeleted() {
        if (mContext instanceof MyReviewStoryActivity) {
            ((MyReviewStoryActivity)mContext).deleteRecommendSchedule(position);
        } else if (mContext instanceof MyCommentActivity) {
            ((MyCommentActivity)mContext).deleteRecommendSchedule(position);
        } else if (mContext instanceof RecommendListActivity) {
            ((RecommendListActivity)mContext).deleteRecommendSchedule(position);
        }
    }
}
