package com.kop.daegudot.MorePage.MyWishlist;

import android.content.Context;
import android.content.Intent;
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

import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.MorePage.MyWishlist.Database.Wishlist;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private static final String TAG = "WishListAdapter";
    private static Context mContext;
    private ArrayList<Wishlist> mWishList;
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
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
            addToScheduleBtn = itemView.findViewById(R.id.addToSch_btn);
            heart = itemView.findViewById(R.id.heart_btn);
            image = itemView.findViewById(R.id.image);
            
            heart.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Wishlist wishlist = mWishList.get(pos);
            
            if (v.getId() == R.id.wishlist_item) {
                Intent intent = new Intent(mContext, MapMainActivity.class);
                Log.d(TAG, "place id: " + wishlist.placeId);
                intent.putExtra("placeId", wishlist.placeId);
                mContext.startActivity(intent);
            }
            if (v.getId() == R.id.heart_btn) {
                ((MyWishlistActivity) mContext).wishlistHandler.deleteWishlists(wishlist);
                removeItem(pos);
            }
        }
    }
    
    WishlistAdapter(Context context, ArrayList<Wishlist> wishlist) {
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
        Wishlist wishlist = mWishList.get(position);
        
        Log.d(TAG, "position: " + position);
        holder.title.setText(wishlist.attractName);
        holder.address.setText(wishlist.address);
//        holder.summary.setText();
//        holder.image.setImageResource(wishlist);
        holder.heart.setBackgroundResource(R.drawable.full_heart);
    }
    
    
    @Override
    public int getItemCount() {
        return mWishList.size();
    }
    
    public void removeItem(int position) {
        mWishList.remove(position);
        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }
}
