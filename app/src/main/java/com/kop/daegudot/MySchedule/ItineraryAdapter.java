package com.kop.daegudot.MySchedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {
    private static ArrayList<ItineraryInfo> mDay;
    private static Context mContext;
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nthDay;
        TextView detail;
        
        ViewHolder(View itemView) {
            super(itemView);
    
            nthDay = itemView.findViewById(R.id.tv_nthDay);
            detail = itemView.findViewById(R.id.tv_detail);
            
            
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
    }
    
    @Override
    public int getItemCount() {
        return mDay.size();
    }
}
