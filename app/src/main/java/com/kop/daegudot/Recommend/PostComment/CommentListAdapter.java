package com.kop.daegudot.Recommend.PostComment;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;
import com.kop.daegudot.Recommend.DrawerHandler;
import com.kop.daegudot.Recommend.PostItem;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private static final String TAG = "CommentListAdapter";
    
    private Context mContext;
    private ArrayList<CommentItem> mCommentList;
    
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
            // TODO: 내 정보 가져오기
            //  (닉네임 같으면 삭제 버튼 보이게 함)
            if ("샤스".equals(writer.getText().toString())) {
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
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mCommentList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                    
                                    // TODO: DB에서 데이터 삭제하기
                    
                                    itemView.invalidate();
                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
            }
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
        
        holder.setButtonVisibility();
    }
    
    @Override
    public int getItemCount() {
        return mCommentList.size();
    }
}
