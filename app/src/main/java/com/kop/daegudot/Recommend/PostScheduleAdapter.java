package com.kop.daegudot.Recommend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class PostScheduleAdapter extends RecyclerView.Adapter<PostScheduleAdapter.ViewHolder> {
    private static final String TAG = "PostScheduleAdapter";
    private Context mContext;
    LayoutInflater li;
    private ArrayList<PostScheduleItem> mPostScheduleList;
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nthday;
        private ChipGroup chipGroup;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            nthday = itemView.findViewById(R.id.nthday);
            chipGroup = itemView.findViewById(R.id.address_chipG);
        }
    
    }
    
    public PostScheduleAdapter(Context context, ArrayList<PostScheduleItem> scheduleList) {
        mContext = context;
        mPostScheduleList = scheduleList;
    }
    
    @NonNull
    @Override
    public PostScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_post_schedule, parent, false);
        
        li = LayoutInflater.from(parent.getContext());
        
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PostScheduleAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "viewholder!!");
        String day = mPostScheduleList.get(position).getDay() + " 일차";
        holder.nthday.setText(day);
        int n = mPostScheduleList.get(position).getPlaceName().size();
        Chip[] chips = new Chip[n];
        Log.d(TAG, "position: " + position + ", n: " + n);
        Log.d(TAG, "mpostschedule: " + mPostScheduleList.get(position).getDay());
        for(int i = 0; i < n; i++) {
            chips[i] = (Chip) li.inflate(R.layout.layout_chip_choice, holder.chipGroup, false);
            chips[i].setText(mPostScheduleList.get(position).getPlaceName().get(i));
            chips[i].setTag(i);
            chips[i].setId(i);
            holder.chipGroup.addView(chips[i]);
        }
        
    }
    
    @Override
    public int getItemCount() {
        return mPostScheduleList.size();
    }
}
