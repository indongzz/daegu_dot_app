package com.kop.daegudot.Recommend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.R;

import java.util.ArrayList;

/**
 * 추천글 List를 띄우는 RecyclerView의 Adapter
 * 추천글 목록에 제목, 별점, 작성자, 댓글 View를 설정
 * 목록 중 하나 선택 시 Drawer를 펼침
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static final String TAG = "PostAdapter";
    
    private static Context mContext;
    private static ArrayList<RecommendResponse> mRecommendList;
    
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
            ((RecommendListActivity) mContext).mDrawerViewControl
                    .openDrawer(mRecommendList.get(getAdapterPosition()), getAdapterPosition());
        }
        
    }
    
    PostAdapter(Context context, ArrayList<RecommendResponse> recommendList) {
        mContext = context;
        mRecommendList = recommendList;
    }
    
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_post_list, parent,false);
    
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        RecommendResponse recommendResponse = mRecommendList.get(position);
        holder.title.setText(recommendResponse.title);
        holder.ratingBar.setRating((float) recommendResponse.star);
        holder.writer.setText(recommendResponse.userResponseDto.nickname);
//        holder.comment.setText(mRecommendList.get(position).getCommentString());
    }
    
    @Override
    public int getItemCount() {
        return mRecommendList.size();
    }
}
