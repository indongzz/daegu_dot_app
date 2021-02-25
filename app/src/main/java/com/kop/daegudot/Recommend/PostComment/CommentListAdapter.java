package com.kop.daegudot.Recommend.PostComment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private static final String TAG = "CommentListAdapter";
    
    private Context mContext;
    private ArrayList<CommentItem> mCommentList;
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
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
            
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: delete comment from list
                }
            });
        }
    }
    
    public CommentListAdapter(Context context, ArrayList<CommentItem> commentList) {
        mContext = context;
        mCommentList = commentList;
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
        CommentItem item = mCommentList.get(position);
        
        holder.writer.setText(item.getWriter());
        holder.content.setText(item.getContent());
        holder.time.setText(item.getStringTime());
    }
    
    @Override
    public int getItemCount() {
        return mCommentList.size();
    }
}
