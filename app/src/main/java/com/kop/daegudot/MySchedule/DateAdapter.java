package com.kop.daegudot.MySchedule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private static ArrayList<DateInfo> mDateList;
    private static Context mContext;
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        Button button;
        
        ViewHolder(View itemView) {
            super(itemView);
            
            button = itemView.findViewById(R.id.btn_datelist);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    DateInfo dateInfo = mDateList.get(pos);
                    Toast.makeText(mContext, dateInfo.getTextString(), Toast.LENGTH_SHORT).show();
    
                    Bundle data = new Bundle();
                    data.putString("first", dateInfo.getFirstDate());
                    data.putString("last", dateInfo.getLastDate());
                    
                    ItineraryDialog dialog = new ItineraryDialog(mContext, data);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();
                }
            });
        }
    }
    
    DateAdapter(Context context, ArrayList<DateInfo> list) {
        mContext = context;
        mDateList = list;
    }
    
    @NonNull
    @Override
    public DateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View view = inflater.inflate(R.layout.layout_datelist_item, parent, false);
        DateAdapter.ViewHolder vh = new ViewHolder(view);
        
        return vh;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String textDate = mDateList.get(position).getTextString();
        holder.button.setText(textDate);
    }
    
    @Override
    public int getItemCount() {
        return mDateList.size();
    }
    
}
