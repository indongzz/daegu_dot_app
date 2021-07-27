package com.kop.daegudot.Recommend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.MySchedule.DateSubSchedule;
import com.kop.daegudot.Network.Schedule.SubScheduleResponse;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class PostScheduleAdapter extends RecyclerView.Adapter<PostScheduleAdapter.ViewHolder> {
    private static final String TAG = "PostScheduleAdapter";
    private Context mContext;
    LayoutInflater li;
    private ArrayList<DateSubSchedule> mDateSubSchedule;
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nthday;
        private ChipGroup chipGroup;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            nthday = itemView.findViewById(R.id.nthday);
            chipGroup = itemView.findViewById(R.id.address_chipG);
        }
    
    }
    
    public PostScheduleAdapter(Context context, ArrayList<DateSubSchedule> dateSubSchedules) {
        mContext = context;
        mDateSubSchedule = dateSubSchedules;
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
        String dayText = position + 1 + "일차";
        holder.nthday.setText(dayText);
        
        int n = mDateSubSchedule.get(position).subScheduleList.size();
        Chip[] chips = new Chip[n];
        for(int i = 0; i < n; i++) {
            SubScheduleResponse subScheduleResponse
                    = mDateSubSchedule.get(position).subScheduleList.get(i);
            chips[i] = (Chip) li.inflate(R.layout.layout_chip_choice, holder.chipGroup, false);
            chips[i].setText(subScheduleResponse.placesResponseDto.attractName);
            chips[i].setTag(subScheduleResponse.placesResponseDto.attractName);
            chips[i].setId(i);
            holder.chipGroup.addView(chips[i]);
            chips[i].setOnClickListener(v -> {
                // Todo: 한번씩 안될 때 있음 고치기
                Intent intent = new Intent(mContext, MapMainActivity.class);
                intent.putExtra("markerPlace",
                        mDateSubSchedule.get(holder.getAdapterPosition())
                                .subScheduleList.get(v.getId()).placesResponseDto);
                mContext.startActivity(intent);
            });
        }
        
    }
    
    @Override
    public int getItemCount() {
        return mDateSubSchedule.size();
    }
}
