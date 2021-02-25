package com.kop.daegudot.Recommend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static final String TAG = "PostAdapter";
    
    private static Context mContext;
    private ArrayList<PostItem> mPostList;
    
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            Log.i(TAG, "id: " + itemView.getId());
            Log.i(TAG, "context:" + mContext);
            ((RecommendListActivity) mContext).openDrawer(itemView.getId());
        }
        
    }
    
    PostAdapter(Context context, ArrayList<PostItem> postList) {
        Log.i(TAG, "init");
        mContext = context;
        mPostList = postList;
        Log.i(TAG, "Adapter context: " + mContext);
    }
    
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_post_list, parent,false);
    
        Log.i(TAG, "ViewHolder");
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
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
