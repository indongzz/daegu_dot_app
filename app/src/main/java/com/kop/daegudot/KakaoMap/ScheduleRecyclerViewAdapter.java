package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.content.DialogInterface;
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

public class ScheduleRecyclerViewAdapter
        extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder> {
        // implements ScheduleItemTouchHelperCallback.OnItemMoveListener {  // drag and swipe
    private Context mContext;
    private ArrayList<String> mNthSchedule;
    
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
            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
            builder.setTitle("삭제");
            builder.setMessage("해당 항목을 삭제하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mNthSchedule.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            
                            // TODO: DB에서 데이터 삭제하기
                            
                            itemView.invalidate();
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
    
    ScheduleRecyclerViewAdapter(ArrayList<String> nthSchedule, Context context) {
        mContext = context;
        mNthSchedule = nthSchedule;
    }
    
    @NonNull
    @Override
    public ScheduleRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
        View view = inflater.inflate(R.layout.bs_item_view, parent, false);
    
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ScheduleRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.text.setText(mNthSchedule.get(position));
    }
    
    @Override
    public int getItemCount() {
        return mNthSchedule.size();
    }
    
    // drag to move, swipe to delete
//    @Override
//    public boolean onItemMove(int fromPosition, int toPosition) {
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mSubScheduleList, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mSubScheduleList, i, i - 1);
//            }
//        }
//
//        notifyItemMoved(fromPosition, toPosition);
//
//        return true;
//    }
//
//    @Override
//    public void onItemSwipe(int position) {
//            mSubScheduleList.remove(position);
//            notifyItemRemoved(position);
//    }
    
}
