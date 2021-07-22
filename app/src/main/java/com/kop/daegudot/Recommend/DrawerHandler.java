package com.kop.daegudot.Recommend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.MorePage.MyCommentActivity;
import com.kop.daegudot.MorePage.MyReview.MyReviewStoryActivity;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.PostComment.CommentItem;
import com.kop.daegudot.Recommend.PostComment.CommentListAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 글 목록에서 글 선택 시 나오는 DrawerLayout
 * 추천글, 내가 쓴 글, 내가 쓴 댓글 볼 때 사용됨
 */
public class DrawerHandler implements PopupMenu.OnMenuItemClickListener {
    private final static String TAG = "DrawerHandler";
    RecommendResponse mRecommendPost;
    Context mContext;
    View mView;
    int position;   // to delete arraylist
    
    ImageButton drawerBackbtn;
    ImageButton menuBtn;
    TextView postTitle, postWriter, postContent;
    RatingBar postRating;
    EditText editComment;
    Button applyCommentBtn, watchScheduleBtn;
    
    RecyclerView mCommentRecyclerview;
    CommentListAdapter mAdapter;
    ArrayList<CommentItem> mCommentList;
    
    InputMethodManager keyboard;
    
    /* Rx java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mToken;
    
    public DrawerHandler(Context context, View view, RecommendResponse post, int position) {
        mContext = context;
        mRecommendPost = post;
        mView = view;
        this.position = position;
    
        bindView();
        prepareComment();
        handleComment();
    }
    
    public void bindView() {
        drawerBackbtn = mView.findViewById(R.id.drawer_backBtn);
        menuBtn = mView.findViewById(R.id.menu_option);
        postTitle = mView.findViewById(R.id.post_title);
        postWriter = mView.findViewById(R.id.post_writer);
        postRating = mView.findViewById(R.id.post_rating);
        postContent = mView.findViewById(R.id.post_content);
        editComment = mView.findViewById(R.id.comment_edit);
        applyCommentBtn = mView.findViewById(R.id.apply_comment_btn);
        mCommentRecyclerview = mView.findViewById(R.id.comment_list);
        
        watchScheduleBtn = mView.findViewById(R.id.btn_schedule);
        
        keyboard = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    
    public void setDrawer() {
        SharedPreferences pref = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        
        postTitle.setText(mRecommendPost.title);
        postRating.setRating((float) mRecommendPost.star);
        // Todo: add writer
//        postWriter.setText(mRecommendPost.getWriter());
        postContent.setText(mRecommendPost.content);
        
        mAdapter = new CommentListAdapter(mContext, mCommentList);
        mCommentRecyclerview.setAdapter(mAdapter);
        mCommentRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
    
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mCommentRecyclerview.getContext(), DividerItemDecoration.VERTICAL);
        mCommentRecyclerview.addItemDecoration(dividerItemDecoration);
    
        watchScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 일정 보기
                PostScheduleBottomSheetDialog postScheduleBottomSheetDialog = new PostScheduleBottomSheetDialog();
                postScheduleBottomSheetDialog
                        .show(((RecommendListActivity)mContext).getFM(),
                                PostScheduleBottomSheetDialog.TAG);
            }
        });
        
        drawerBackbtn.setOnClickListener(v -> {
            callCloseDrawer();
            editComment.setText("");
        });
        
        // TODO: 작성자가 나인 경우에만 버튼이 보이게 함
        menuBtn.setVisibility(View.VISIBLE);
        menuBtn.setOnClickListener(this::showMenu);
    }
    
    public void handleComment() {
        // 댓글 등록 버튼 클릭 이벤트
        applyCommentBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO: DB에 댓글 추가
                CommentItem newComment = new CommentItem();
                // TODO: 나의 정보에서 닉네임 가져오기
                newComment.setWriter("샤스");
                newComment.setContent(editComment.getText().toString());
                newComment.setTime(LocalDateTime.now());
                
                mCommentList.add(newComment);
                
                editComment.setText("");
                editComment.clearFocus();
                keyboard.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
            }
        });
    }
    
    // TODO: get comments from DB
    public void prepareComment() {
        mCommentList = new ArrayList<>();
        
        String[] writers = {"작성자1", "작성자2"};
        String[] contents = {"내용내용내용1", "내용내욘애뇽3"};
        LocalDateTime[] times = {LocalDateTime.parse("2021-02-25T19:30:00"), LocalDateTime.parse("2021-02-25T19:37:00")};
        
        for (int i = 0; i < 2; i++) {
            CommentItem item = new CommentItem();
            item.setWriter(writers[i]);
            item.setContent(contents[i]);
            item.setTime(times[i]);
            
            mCommentList.add(item);
        }
    }
    
    // menu option
    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(mContext, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_option, popup.getMenu());
        popup.show();
    }
    
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            Log.d("Menu: ", "delete clicked");
            deleteRecommendScheduleRx();
            return true;
        }
        if (item.getItemId() == R.id.menu_update) {
            Log.d("Menu: ", "update clicked");
            Intent intent = new Intent(mContext, AddRecommendActivity.class);
            intent.putExtra("recommendPost", mRecommendPost);
            mContext.startActivity(intent);
            return true;
        }
        return false;
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
                        callCloseDrawer();
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
        // Todo: make delete function on MyReviewStoryActivity and MyCommentActivity
        if (mContext instanceof MyReviewStoryActivity) {
            ((MyReviewStoryActivity)mContext).mMyReviewAndCommentAdapter.notifyDataSetChanged();
        } else if (mContext instanceof MyCommentActivity) {
            ((MyCommentActivity)mContext).mMyReviewAndCommentAdapter.notifyDataSetChanged();
        } else if (mContext instanceof RecommendListActivity) {
            ((RecommendListActivity)mContext).deleteRecommendSchedule(position);
        }
    }
    
    public void callCloseDrawer() {
        if (mContext instanceof MyReviewStoryActivity) {
            ((MyReviewStoryActivity)mContext).mDrawerViewControl.closeDrawer();
        }
        else if (mContext instanceof MyCommentActivity) {
            ((MyCommentActivity)mContext).mDrawerViewControl.closeDrawer();
        }
        else if (mContext instanceof RecommendListActivity) {
            ((RecommendListActivity)mContext).mDrawerViewControl.closeDrawer();
        }
    }
}





