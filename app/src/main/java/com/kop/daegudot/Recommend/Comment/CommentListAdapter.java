package com.kop.daegudot.Recommend.Comment;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.MorePage.MyReview.MyCommentActivity;
import com.kop.daegudot.MorePage.MyReview.MyReviewStoryActivity;
import com.kop.daegudot.Network.Recommend.Comment.CommentResponse;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.RecommendListActivity;

import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private static final String TAG = "CommentListAdapter";
    
    private Context mContext;
    private ArrayList<CommentResponse> mCommentList;
    private UserResponse mUser;
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView writer;
        private TextView content;
        private TextView time;
        private Button deleteBtn;
    
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            writer = itemView.findViewById(R.id.comment_writer);
            content = itemView.findViewById(R.id.comment_content);
            time = itemView.findViewById(R.id.comment_time);
            deleteBtn = itemView.findViewById(R.id.comment_delete);
            
            deleteBtn.setOnClickListener(this);
        }
        
        public void setButtonVisibility() {
            if (mContext instanceof RecommendListActivity) {
                if ((mUser.token).equals(mCommentList.get(getAdapterPosition()).userResponseDto.token)) {
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }
            else if (mContext instanceof MyReviewStoryActivity ||
                    mContext instanceof MyCommentActivity) {
                deleteBtn.setVisibility(View.VISIBLE);
            }
        }
    
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.comment_delete:
                    final AlertDialog.Builder builder =
                            new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
                    builder.setTitle("삭제");
                    builder.setMessage("댓글을 삭제하시겠습니까?");
                    builder.setPositiveButton("예",
                            (dialog, which) -> {
                                int position = getAdapterPosition();

                                ((RecommendListActivity)mContext)
                                        .mDrawerViewControl.mDrawerHandler.mCommentHandler
                                        .deleteComment(mCommentList.get(position));
    
                                mCommentList.remove(position);
                                notifyItemRemoved(position);
                                itemView.invalidate();
                            });
                    builder.setNegativeButton("아니오",
                            (dialog, which) -> dialog.cancel());
                    builder.show();
            }
        }
    }
    
    public CommentListAdapter(Context context, ArrayList<CommentResponse> commentList) {
        mContext = context;
        mCommentList = commentList;
        if (mContext instanceof RecommendListActivity) {
            mUser = ((RecommendListActivity) mContext).getUser();
        }
    }
    
    @NonNull
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_comment, parent, false);
        
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.ViewHolder holder, int position) {
        CommentResponse item = mCommentList.get(position);
        
        holder.writer.setText(item.userResponseDto.nickname);
        holder.content.setText(item.comments);
        holder.time.setText(item.getDateTime());
        
        holder.setButtonVisibility();
    }
    
    @Override
    public int getItemCount() {
        return mCommentList.size();
    }
}
