package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.Collections;

public class ScheduleBSAdapter extends RecyclerView.Adapter<ScheduleBSAdapter.ViewHolder> implements ScheduleItemTouchHelperCallback.OnItemMoveListener {
    private Context mContext;
    private ArrayList<String> mNameList;
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        Button button;
        TextView text;
        
        ViewHolder(View itemView) {
            super(itemView);
        
            button = itemView.findViewById(R.id.circleBtn);
            text = itemView.findViewById(R.id.tv_btnName);
            
            itemView.setOnLongClickListener(this);
        }
    
        @Override
        public boolean onLongClick(View v) {
            deleteSubschedule();
            return true;
        }
        
        public void deleteSubschedule() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
            builder.setTitle("삭제");
            builder.setMessage("해당 항목을 삭제하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mNameList.remove(getAdapterPosition());
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
        holder.text.setText(mNameList.get(position));
    }
    
    @Override
    public int getItemCount() {
        return mNameList.size();
    }
    
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mNameList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mNameList, i, i - 1);
            }
        }
    
        notifyItemMoved(fromPosition, toPosition);
        
        return true;
    }
    
    @Override
    public void onItemSwipe(int position) {
            mNameList.remove(position);
            notifyItemRemoved(position);
    }
    
}
