package com.kop.daegudot.Recommend;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kop.daegudot.Network.Recommend.Comment.CommentRegister;
import com.kop.daegudot.Network.Recommend.Comment.CommentResponse;
import com.kop.daegudot.Network.Recommend.Comment.CommentResponseList;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CommentHandler {
    private static final String TAG = "CommentHandler";
    Context mContext;
    View mView;
    ArrayList<CommentResponse> mCommentList;
    RecommendResponse mRecommendPost;

    /* Rx java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mToken = null;

    public CommentHandler(Context context, View view, RecommendResponse recommendResponse) {
        mContext = context;
        mView = view;
        mRecommendPost = recommendResponse;

        SharedPreferences pref = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");

        selectAllCommentListRx();
    }

    /* Register Comment */

    public void registerComment(CommentRegister commentRegister) {
        registerCommentRx(commentRegister);
    }

    private void registerCommentRx(CommentRegister commentRegister) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<Long> observable = service.registerComment(commentRegister);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.d("RX " + TAG, "Next");

                        if (response == 1L) {
                            CommentResponse commentResponse = new CommentResponse();
                            commentResponse.id = response;
                            commentResponse.comments = commentRegister.comment;
                            commentResponse.recommendScheduleResponseDto = mRecommendPost;
                            // 시간 ? 어쩌지
//                            commentResponse.dateTime = ;
                            commentResponse.userResponseDto = ((RecommendListActivity)mContext).getUser();
                            mCommentList.add(commentResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");

                    }
                })
        );
    }

    /* Select All Comment List */
    public ArrayList<CommentResponse> getCommentList() {
        return mCommentList;
    }

    private void selectAllCommentListRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<CommentResponseList> observable = service.selectAllCommentList(mRecommendPost.id);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CommentResponseList>() {
                    @Override
                    public void onNext(CommentResponseList response) {
                        Log.d("RX " + TAG, "Next");

                        if (response.status == 1L) {
                            mCommentList = response.commentResponseDtoArrayList;
                        } else if (response.status == 0L) {
                            Toast.makeText(mContext,
                                    "댓글을 읽어올 수 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");

                    }
                })
        );
    }

    /* Delete Comment */
    public void deleteComment(CommentResponse commentResponse) {
        deleteCommentRx(commentResponse);
    }

    private void deleteCommentRx(CommentResponse commentResponse) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<Long> observable = service.deleteComment(commentResponse.id);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.d("RX " + TAG, "Next");
                        if (response == 1L) {
                            Toast.makeText(mContext, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");

                    }
                })
        );
    }
}

