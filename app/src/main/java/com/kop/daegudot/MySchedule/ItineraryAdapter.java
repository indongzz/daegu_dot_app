package com.kop.daegudot.MySchedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {
    private static ArrayList<ItineraryInfo> mDay;
    private static Context mContext;
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nthDay;
        TextView detailAddress;
        
        ViewHolder(View itemView) {
            super(itemView);
    
            nthDay = itemView.findViewById(R.id.tv_nthDay);
            detailAddress = itemView.findViewById(R.id.tv_detail);
            
            TextView.OnClickListener onClickListener = new TextView.OnClickListener() {
    
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
    
                    if (pos != RecyclerView.NO_POSITION) {
                        // TODO: 클릭 시 지도 화면으로 넘어가기
                        String whatDay = mDay.get(pos).getDate();
                        Toast.makeText(mContext, whatDay, Toast.LENGTH_SHORT).show();
                    }
                }
            };
    
            nthDay.setOnClickListener(onClickListener);
            detailAddress.setOnClickListener(onClickListener);
        }
        
    }
    
    ItineraryAdapter(Context context, ArrayList<ItineraryInfo> list) {
        mContext = context;
        mDay = list;
    }
    
    @NonNull
    @Override
    public ItineraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View view = inflater.inflate(R.layout.layout_itinerarylist_item, parent, false);
        ItineraryAdapter.ViewHolder vh = new ItineraryAdapter.ViewHolder(view);
        
        return vh;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItineraryInfo info = mDay.get(position);
        holder.nthDay.setText(info.getDate());
        holder.detailAddress.setText(info.getAddress());
    }
    
    @Override
    public int getItemCount() {
        return mDay.size();
    }
    
    
}
