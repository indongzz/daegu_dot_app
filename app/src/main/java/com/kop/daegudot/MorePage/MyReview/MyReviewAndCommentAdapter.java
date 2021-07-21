package com.kop.daegudot.MorePage.MyReview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.MorePage.MyCommentActivity;
import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.PostItem;

import java.util.ArrayList;

/**
 * 더보기 - 내가 쓴 글, 내가 쓴 댓글의 리스트를 띄우기 위한 RecyclerView Adapter
 */
public class MyReviewAndCommentAdapter extends RecyclerView.Adapter<MyReviewAndCommentAdapter.ViewHolder> {
    private static final String TAG = "MyReViewAndCommentAdapter";
    private static Context mContext;
    private ArrayList<PostItem> mPostList;
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private RatingBar ratingBar;
        private TextView writer;
        private TextView comment;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            title = itemView.findViewById(R.id.writing_title);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            writer = itemView.findViewById(R.id.writer);
            comment = itemView.findViewById(R.id.comment);
            
            itemView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            if (mContext instanceof MyReviewStoryActivity) {
                ((MyReviewStoryActivity) mContext).mDrawerViewControl.openDrawer(itemView.getId());
            } else if (mContext instanceof MyCommentActivity) {
                ((MyCommentActivity) mContext).mDrawerViewControl.openDrawer(itemView.getId());
            }
        }
        
    }
    
    public MyReviewAndCommentAdapter(Context context, ArrayList<PostItem> postList) {
        mContext = context;
        mPostList = postList;
    }
    
    @NonNull
    @Override
    public MyReviewAndCommentAdapter.ViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_post_list, parent,false);
        
        Log.i(TAG, "ViewHolder");
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MyReviewAndCommentAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "position: " + position);
        
        holder.title.setText(mPostList.get(position).getTitle());
        holder.ratingBar.setRating(mPostList.get(position).getRating());
        holder.writer.setText(mPostList.get(position).getWriter());
        holder.comment.setText(mPostList.get(position).getCommentString());
        holder.itemView.setId(mPostList.get(position).getId());
    }
    
    
    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}
