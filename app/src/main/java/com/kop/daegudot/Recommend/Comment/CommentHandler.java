package com.kop.daegudot.Recommend.Comment;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kop.daegudot.MorePage.MyReview.MyCommentActivity;
import com.kop.daegudot.MorePage.MyReview.MyReviewStoryActivity;
import com.kop.daegudot.Network.Recommend.Comment.CommentRegister;
import com.kop.daegudot.Network.Recommend.Comment.CommentResponse;
import com.kop.daegudot.Network.Recommend.Comment.CommentResponseList;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.Recommend.RecommendListActivity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 추천글 댓글 추가, 조회, 삭제, 수정
 */

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
        
        mCommentList = new ArrayList<>();
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
                        Log.d("RX " + TAG, "comment register Next");
    
                        CommentResponse commentResponse = new CommentResponse();
                        commentResponse.id = response;
                        commentResponse.comments = commentRegister.comments;
                        commentResponse.recommendScheduleResponseDto = mRecommendPost;
                        // Todo: 시간 ? 어쩌지
                        commentResponse.dateTime = LocalDateTime.now().toString();
                        commentResponse.userResponseDto = getUserByActivity();
                        
                        addCommentOnActivity(commentResponse);
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
    
    private UserResponse getUserByActivity() {
        if (mContext instanceof RecommendListActivity) {
            return ((RecommendListActivity) mContext).getUser();
        }
        else if (mContext instanceof MyReviewStoryActivity) {
            return ((MyReviewStoryActivity) mContext).getUser();
        }
        else if (mContext instanceof MyCommentActivity) {
            return ((MyCommentActivity) mContext).getUser();
        }
        return null;
    }
    
    private void addCommentOnActivity(CommentResponse commentResponse) {
        if (mContext instanceof RecommendListActivity) {
            ((RecommendListActivity) mContext).mDrawerViewControl
                    .mDrawerHandler.addComment(commentResponse);
        }
        else if (mContext instanceof MyReviewStoryActivity) {
            ((MyReviewStoryActivity) mContext).mDrawerViewControl
                    .mDrawerHandler.addComment(commentResponse);
        }
        else if (mContext instanceof MyCommentActivity) {
            ((MyCommentActivity) mContext).mDrawerViewControl
                    .mDrawerHandler.addComment(commentResponse);
        }
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
                            // 댓글 없음
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                        Toast.makeText(mContext,
                                "댓글을 읽어올 수 없습니다", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");
                        callUpdateCommentUI();
                    }
                })
        );
    }
    
    private void callUpdateCommentUI() {
        if (mContext instanceof RecommendListActivity) {
            ((RecommendListActivity) mContext).mDrawerViewControl
                    .mDrawerHandler.updateCommentUI();
        }
        else if (mContext instanceof MyReviewStoryActivity) {
            ((MyReviewStoryActivity) mContext).mDrawerViewControl
                    .mDrawerHandler.updateCommentUI();
        }
        else if (mContext instanceof MyCommentActivity) {
            ((MyCommentActivity) mContext).mDrawerViewControl
                    .mDrawerHandler.updateCommentUI();
        }
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

