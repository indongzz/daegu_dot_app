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

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {
    private static ArrayList<ItineraryInfo> mItineraryList;
    private static ArrayList<DateInfo> mDateList;
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
                        String whatDay = mItineraryList.get(pos).getDate();
                        Toast.makeText(mContext, whatDay, Toast.LENGTH_SHORT).show();
    
                        Intent intent = new Intent(mContext, MapMainActivity.class);
                        intent.putParcelableArrayListExtra("DateList", mDateList);
                        intent.putParcelableArrayListExtra("ItineraryList", mItineraryList);
                        mContext.startActivity(intent);
                    }
                }
            };
    
            nthDay.setOnClickListener(onClickListener);
            detailAddress.setOnClickListener(onClickListener);
        }
        
    }
    
    ItineraryAdapter(Context context, ArrayList<ItineraryInfo> itineraryList, ArrayList<DateInfo> dateList) {
        mContext = context;
        mItineraryList = itineraryList;
        mDateList = dateList;
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
        ItineraryInfo info = mItineraryList.get(position);
        holder.nthDay.setText(info.getDate());
        holder.detailAddress.setText(info.getAddressString());
    }
    
    @Override
    public int getItemCount() {
        return mItineraryList.size();
    }
    
    
}
