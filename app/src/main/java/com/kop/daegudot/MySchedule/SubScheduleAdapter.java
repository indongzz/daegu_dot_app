package com.kop.daegudot.MySchedule;

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

//import com.kop.daegudot.KakaoMap.ConvertToMapMainAcitivity;
import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.Network.Schedule.SubScheduleResponse;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class SubScheduleAdapter extends RecyclerView.Adapter<SubScheduleAdapter.ViewHolder> {
    private static ArrayList<DateSubSchedule> mDateSubScheduleList;
    private static MainScheduleInfo mMainSchedule;
    private static Context mContext;
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nthDay;
        TextView detailAddress;
        
        ViewHolder(View itemView) {
            super(itemView);
            
            nthDay = itemView.findViewById(R.id.tv_nthDay);
            detailAddress = itemView.findViewById(R.id.tv_detail);
            
            nthDay.setOnClickListener(this);
            detailAddress.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            
            if (pos != RecyclerView.NO_POSITION) {
                // 세부 일정 클릭하여 MapMainActivity로 넘어가기
                String whatDay = mDateSubScheduleList.get(pos).date;
                Toast.makeText(mContext, whatDay, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, MapMainActivity.class);
                intent.putExtra("mainSchedule", mMainSchedule);
                intent.putParcelableArrayListExtra("dateSubSchedule", mDateSubScheduleList);
                intent.putExtra("position", pos);
                mContext.startActivity(intent);
            }
        }
        
    }
    
    SubScheduleAdapter(Context context, ArrayList<DateSubSchedule> dateSubSchedules, MainScheduleInfo mainScheduleInfo) {
        mContext = context;
        mDateSubScheduleList = dateSubSchedules;
        mMainSchedule = mainScheduleInfo;
    }
    
    @NonNull
    @Override
    public SubScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View view = inflater.inflate(R.layout.layout_subschedule_item, parent, false);
        SubScheduleAdapter.ViewHolder vh = new SubScheduleAdapter.ViewHolder(view);
        
        return vh;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateSubSchedule dateSubSchedule = mDateSubScheduleList.get(position);
        
        String str = (position + 1) + "일차 - " + dateSubSchedule.date.substring(5, 7) + "." +
                    dateSubSchedule.date.substring(8, 10);
        holder.nthDay.setText(str);
        
        String texts = "";
        for (int i = 0; i < dateSubSchedule.subScheduleList.size(); i++) {
            texts += dateSubSchedule.subScheduleList.get(i).placesResponseDto.attractName + "\n";
        }
        holder.detailAddress.setText(texts);
    }
    
    @Override
    public int getItemCount() {
        return mDateSubScheduleList.size();
    }
    

}
