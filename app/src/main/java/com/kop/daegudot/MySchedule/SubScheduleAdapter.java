package com.kop.daegudot.MySchedule;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class SubScheduleAdapter extends RecyclerView.Adapter<SubScheduleAdapter.ViewHolder> {
    private static ArrayList<SubScheduleInfo> mSubScheduleList;
    private static ArrayList<MainScheduleInfo> mMainScheduleList;
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
                        String whatDay = mSubScheduleList.get(pos).getDate();
                        Toast.makeText(mContext, whatDay, Toast.LENGTH_SHORT).show();
    
                        Intent intent = new Intent(mContext, MapMainActivity.class);
                        intent.putParcelableArrayListExtra("MainScheduleList", mMainScheduleList);
                        intent.putParcelableArrayListExtra("SubScheduleList", mSubScheduleList);
                        mContext.startActivity(intent);
                    }
                }
            };
    
            nthDay.setOnClickListener(onClickListener);
            detailAddress.setOnClickListener(onClickListener);
        }
        
    }
    
    SubScheduleAdapter(Context context, ArrayList<SubScheduleInfo> itineraryList, ArrayList<MainScheduleInfo> dateList) {
        mContext = context;
        mSubScheduleList = itineraryList;
        mMainScheduleList = dateList;
    }
    
    @NonNull
    @Override
    public SubScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View view = inflater.inflate(R.layout.layout_itinerarylist_item, parent, false);
        SubScheduleAdapter.ViewHolder vh = new SubScheduleAdapter.ViewHolder(view);
        
        return vh;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubScheduleInfo info = mSubScheduleList.get(position);
        holder.nthDay.setText(info.getDate());
        holder.detailAddress.setText(info.getAddressString());
    }
    
    @Override
    public int getItemCount() {
        return mSubScheduleList.size();
    }
    
    
}
