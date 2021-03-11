package com.kop.daegudot.Recommend;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.PostComment.CommentItem;
import com.kop.daegudot.Recommend.PostComment.CommentListAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class DrawerHandler {
    private final static String TAG = "DrawerHandler";
    PostItem mPost;
    Context mContext;
    View mView;
    
    TextView postTitle, postWriter, postContent;
    RatingBar postRating;
    EditText editComment;
    Button applyCommentBtn, watchScheduleBtn;
    
    RecyclerView mCommentRecyclerview;
    CommentListAdapter mAdapter;
    ArrayList<CommentItem> mCommentList;
    
    InputMethodManager keyboard;
    
    DrawerHandler(Context context, View view, PostItem post) {
        mContext = context;
        mPost = post;
        mView = view;
        
        bindView();
        prepareComment();
        handleComment();
    }
    
    public void bindView() {
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
        postTitle.setText(mPost.getTitle());
        postRating.setRating(mPost.getRating());
        postWriter.setText(mPost.getWriter());
        postContent.setText(mPost.getContent());
        
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
}





