package com.kop.daegudot.MorePage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.KakaoMap.MarkerInfo;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private static final String TAG = "WishListAdapter";
    private static Context mContext;
    private ArrayList<MarkerInfo> mWishList;
    
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private RatingBar rating;
        private TextView address;
        private TextView summary;
        private Button addToScheduleBtn;
        private Button heart;
        ImageView image;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            title = itemView.findViewById(R.id.tv_title);
            address = itemView.findViewById(R.id.tv_address);
            summary = itemView.findViewById(R.id.tv_spec);
            rating = itemView.findViewById(R.id.rating_bar);
            addToScheduleBtn = itemView.findViewById(R.id.addToSch_btn);
            heart = itemView.findViewById(R.id.heart_btn);
            image = itemView.findViewById(R.id.image);
            
            itemView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            Log.i(TAG, "id: " + itemView.getId());
            Log.i(TAG, "context:" + mContext);
        }
    }
    
    WishlistAdapter(Context context, ArrayList<MarkerInfo> wishlist) {
        mContext = context;
        mWishList = wishlist;
    }
    
    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wishlist_item, parent,false);
        
        WishlistAdapter.ViewHolder vh = new WishlistAdapter.ViewHolder(view);
        return vh;
    }
    
    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        
        Log.i(TAG, "position: " + position);
        Log.i(TAG, "myname: " + mWishList.get(0).getName());
        holder.title.setText(mWishList.get(position).getName());
        holder.rating.setRating(mWishList.get(position).getRate());
        holder.address.setText(mWishList.get(position).getAddress());
        holder.summary.setText(mWishList.get(position).getSummary());
        holder.image.setImageResource(mWishList.get(position).getImage());
        holder.heart.setBackgroundResource(R.drawable.full_heart);
        holder.heart.setTag(holder.getAdapterPosition());
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                removeItem(pos);
                Log.d(TAG, "pos : " + pos);
            }
        });
    }
    
    
    @Override
    public int getItemCount() {
        return mWishList.size();
    }
    
    public void removeItem(int position) {
        mWishList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
