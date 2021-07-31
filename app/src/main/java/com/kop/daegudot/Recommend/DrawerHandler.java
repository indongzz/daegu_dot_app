package com.kop.daegudot.Recommend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
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

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.kop.daegudot.MorePage.MyReview.MyCommentActivity;
import com.kop.daegudot.MorePage.MyReview.MyReviewStoryActivity;
import com.kop.daegudot.Network.More.MyInfo.MyCommentList;
import com.kop.daegudot.Network.Recommend.Comment.CommentRegister;
import com.kop.daegudot.Network.Recommend.Comment.CommentResponse;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.Comment.CommentHandler;
import com.kop.daegudot.Recommend.Comment.CommentListAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
    ChipGroup mChipGroup;
    
    public CommentHandler mCommentHandler;
    RecyclerView mCommentRecyclerview;
    public CommentListAdapter mCommentAdapter;
    ArrayList<CommentResponse> mCommentList;
    
    private UserResponse mUser;
    InputMethodManager keyboard;
    
    public DrawerHandler(Context context, View view, RecommendResponse post, int position) {
        mContext = context;
        mRecommendPost = post;
        mView = view;
        this.position = position;
    
        bindView();
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
        mChipGroup = mView.findViewById(R.id.hashtag_groups);
        
        watchScheduleBtn = mView.findViewById(R.id.btn_schedule);
        
        keyboard = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    
    public void setDrawer() {
        postTitle.setText(mRecommendPost.title);
        postRating.setRating((float) mRecommendPost.star);
        postWriter.setText(mRecommendPost.userResponseDto.nickname);
        postContent.setText(mRecommendPost.content);
        
        mChipGroup.removeAllViews();
        
        int n = mRecommendPost.hashtags.size();
        final Chip[] chips = new Chip[n];
        
        for(int i = 0; i < n; i++) {
            LayoutInflater layoutInflater = getLayoutInflaterByActivity();
            
            chips[i] = (Chip) layoutInflater
                    .inflate(R.layout.layout_chip_choice, mChipGroup, false);
            String hashName = "#" + mRecommendPost.hashtags.get(i).content;
            
            chips[i].setText(hashName);
            chips[i].setTag(mRecommendPost.hashtags.get(i).content);
            chips[i].setId((int)mRecommendPost.hashtags.get(i).id);
            chips[i].setCheckable(false);
            chips[i].setChecked(true);
            mChipGroup.addView(chips[i]);
        }
        
        /* 댓글 */
        mCommentHandler = new CommentHandler(mContext, mView, mRecommendPost);
        mCommentRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
    
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mCommentRecyclerview.getContext(), DividerItemDecoration.VERTICAL);
        mCommentRecyclerview.addItemDecoration(dividerItemDecoration);
    
        /* 일정 보기 */
        watchScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 일정 보기
                PostScheduleBottomSheetDialog postScheduleBottomSheetDialog
                        = new PostScheduleBottomSheetDialog(mRecommendPost.mainScheduleResponseDto);
                if (mContext instanceof RecommendListActivity) {
                    postScheduleBottomSheetDialog
                            .show(((RecommendListActivity) mContext).getFM(),
                                    PostScheduleBottomSheetDialog.TAG);
                } else if (mContext instanceof MyReviewStoryActivity) {
                    postScheduleBottomSheetDialog
                            .show(((MyReviewStoryActivity) mContext).getFM(),
                                    PostScheduleBottomSheetDialog.TAG);
                }
            }
        });
        
        drawerBackbtn.setOnClickListener(v -> {
            callCloseDrawer();
            editComment.setText("");
        });
        
        /* 작성자일 때 - 수정, 삭제 메뉴 버튼 */
        if (mContext instanceof RecommendListActivity) {
            mUser = ((RecommendListActivity) mContext).getUser();
            if (mUser.token.equals(mRecommendPost.userResponseDto.token)) {
                showMenuButton();
            }
        } else if (mContext instanceof MyReviewStoryActivity) {
            showMenuButton();
        } else if (mContext instanceof MyCommentActivity) {
            showMenuButton();
        }
    }
    
    public void showMenuButton() {
        menuBtn.setVisibility(View.VISIBLE);
        menuBtn.setOnClickListener(this::showMenu);
    }
    
    public void updateDrawerContent(RecommendResponse recommendResponse) {
        mRecommendPost = recommendResponse;
    }
    
    public LayoutInflater getLayoutInflaterByActivity() {
        if (mContext instanceof RecommendListActivity) {
            return ((RecommendListActivity) mContext).getLayoutInflater();
        }
        else if (mContext instanceof MyReviewStoryActivity) {
            return ((MyReviewStoryActivity) mContext).getLayoutInflater();
        }
        else if (mContext instanceof MyCommentActivity) {
            return ((MyCommentActivity) mContext).getLayoutInflater();
        }
        return null;
    }
    
    /* Comment */
    public void updateCommentUI() {
        mCommentList = mCommentHandler.getCommentList();
        mCommentAdapter = new CommentListAdapter(mContext, mCommentList);
        mCommentRecyclerview.setAdapter(mCommentAdapter);
    }
    
    // Comment Register
    public void handleComment() {
        // 댓글 등록 버튼 클릭 이벤트
        applyCommentBtn.setOnClickListener(v -> {
            CommentRegister newComment = new CommentRegister();
            newComment.comments = editComment.getText().toString();
            newComment.recommendScheduleId = mRecommendPost.id;
            
            mCommentHandler.registerComment(newComment);
            
            editComment.setText("");
            editComment.clearFocus();
            keyboard.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
        });
    }
    
    // Add comment on List and notify
    public void addComment(CommentResponse newComment) {
        mCommentList.add(newComment);
        mCommentAdapter.notifyDataSetChanged();
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
            DeleteRecommendSchedule deleteRecommendSchedule
                    = new DeleteRecommendSchedule(mContext, mRecommendPost, position);
            callCloseDrawer();
            
            return true;
        }
        if (item.getItemId() == R.id.menu_update) {
            Log.d("Menu: ", "update clicked");
            Intent intent = new Intent(mContext, UpdateRecommendActivity.class);
            intent.putExtra("recommendPost", mRecommendPost);
            intent.putExtra("listIndex", position);
            mContext.startActivity(intent);
            return true;
        }
        return false;
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





