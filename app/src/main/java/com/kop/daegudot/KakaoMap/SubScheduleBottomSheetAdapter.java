package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.SubScheduleResponse;
import com.kop.daegudot.R;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SubScheduleBottomSheetAdapter
        extends RecyclerView.Adapter<SubScheduleBottomSheetAdapter.ViewHolder> {
        // implements ScheduleItemTouchHelperCallback.OnItemMoveListener {  // drag and swipe
    private final static String TAG = "SubScheduleBottomSheetAdapter";
    private Context mContext;
    private ArrayList<SubScheduleResponse> mSubScheduleList;
    
    /* RX java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private SharedPreferences pref;
    private String mToken;
    
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
                    (dialog, which) -> {
                        int position = getAdapterPosition();
                        
                        SubScheduleHandler subScheduleHandler = new SubScheduleHandler(mContext);
                        subScheduleHandler.deleteSubSchedule(mSubScheduleList.get(position));
                        
                        mSubScheduleList.remove(position);
                        notifyItemRemoved(position);
                        
                        itemView.invalidate();
                    });
            builder.setNegativeButton("아니오",
                    (dialog, which) -> dialog.cancel());
            builder.show();
        }
    }
    
    SubScheduleBottomSheetAdapter(ArrayList<SubScheduleResponse> subscheduleList, Context context) {
        mContext = context;
        mSubScheduleList = subscheduleList;
    }
    
    @NonNull
    @Override
    public SubScheduleBottomSheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
        View view = inflater.inflate(R.layout.bs_item_view, parent, false);
    
    
        pref = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SubScheduleBottomSheetAdapter.ViewHolder holder, int position) {
        holder.text.setText(mSubScheduleList.get(position).placesResponseDto.attractName);
        
    }
    
    @Override
    public int getItemCount() {
        return mSubScheduleList.size();
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
