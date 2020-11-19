package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.MySchedule.MainScheduleAdapter;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class ScheduleBSAdapter extends RecyclerView.Adapter<ScheduleBSAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mNameList;
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        Button button;
        TextView text;
        
        ViewHolder(View itemView) {
            super(itemView);
        
            button = itemView.findViewById(R.id.circleBtn);
            text = itemView.findViewById(R.id.tv_btnName);
//            button.setOnClickListener(this);
//            button.setOnLongClickListener(this);
        }
        
//        @Override
//        public void onClick(View v) {
//
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            return false;
//        }
    }
    
    ScheduleBSAdapter(ArrayList<String> nameList, Context context) {
        mContext = context;
        mNameList = nameList;
    }
    
    @NonNull
    @Override
    public ScheduleBSAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
        View view = inflater.inflate(R.layout.bs_item_view, parent, false);
    
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ScheduleBSAdapter.ViewHolder holder, int position) {
        Log.d("BindviewHolder", mNameList.get(position) + "*************************");
        holder.text.setText(mNameList.get(position));
    }
    
    @Override
    public int getItemCount() {
        return mNameList.size();
    }
}
