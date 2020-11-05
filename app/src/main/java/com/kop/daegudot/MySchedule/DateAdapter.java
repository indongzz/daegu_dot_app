package com.kop.daegudot.MySchedule;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.Objects;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    private static ArrayList<DateInfo> mDateList;
    private static Context mContext;
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        Button button;
        
        ViewHolder(View itemView) {
            super(itemView);
            
            button = itemView.findViewById(R.id.btn_datelist);
            button.setOnClickListener(this);
            button.setOnLongClickListener(this);
        }
    
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            DateInfo dateInfo = mDateList.get(pos);
            Toast.makeText(mContext, dateInfo.getTextString(), Toast.LENGTH_SHORT).show();
        
            
            ItineraryDialog dialog = new ItineraryDialog(mContext, pos, mDateList, new ItineraryDialog.ItineraryDialogListener() {
                @Override
                public void dialogEventListener() {
                    deleteListItem();
                }
            });
            Objects.requireNonNull(dialog.getWindow())
                    .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();
        }
    
        @Override
        public boolean onLongClick(View v) {
            deleteListItem();
            
            return true;
        }
        
        public void deleteListItem() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
            builder.setTitle("삭제");
            builder.setMessage("해당 항목을 삭제하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mDateList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            
                            // TODO: DB에서 데이터 삭제하기
                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
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
    
        return new ViewHolder(view);
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
