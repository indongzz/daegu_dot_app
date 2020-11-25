package com.kop.daegudot.MySchedule;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class SubScheduleDialog extends Dialog implements View.OnClickListener {   // 세부 일정
    private Context mContext;
    private RecyclerView recyclerView;
    private SubScheduleAdapter adapter;
    String firstDay, lastDay;
    Date[] date;
    MainScheduleInfo mMainScheduleInfo;
    
    private ArrayList<SubScheduleInfo> mSubScheduleList = new ArrayList<>();
    private SubScheduleDialogListener dialogListener;
    
    public SubScheduleDialog(@NonNull Context context, MainScheduleInfo mainScheduleInfo,
                             SubScheduleDialogListener dialogListener) {
        super(context);
        this.mContext = context;
        mMainScheduleInfo = mainScheduleInfo;
        this.dialogListener = dialogListener;
    }
    
    public interface SubScheduleDialogListener {
        void dialogEventListener();
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        
        
        setContentView(R.layout.itinerary_dialog);
        
        firstDay = mMainScheduleInfo.getmFirstDate();
        lastDay = mMainScheduleInfo.getmLastDate();
        
        TextView dialogTitle = findViewById(R.id.dialogTitle);
        String title = mMainScheduleInfo.getButtonString();
        dialogTitle.setText(title);
    
        Button deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(this);
        
        setItineraryText();
    
        recyclerView = findViewById(R.id.itineraryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    
        adapter = new SubScheduleAdapter(getContext(), mSubScheduleList, mMainScheduleInfo);
        recyclerView.setAdapter(adapter);
    }
    
    public void setItineraryText() {
        /* 세부 일정 날짜 */
        
        LocalDate[] dateArray = mMainScheduleInfo.getDateArray();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");
        
        for (int i = 0; i < mMainScheduleInfo.getDateBetween(); i++) {
            // TODO: 그 날짜 세부 일정 불러오기
            SubScheduleInfo data = new SubScheduleInfo();
            String dateText = i + 1 + "일차 - " + dateArray[i].format(formatter);
            
            data.setDate(dateText);
        
            ArrayList<String> address = new ArrayList<>();
        
            address.add("대구 중구 동성로2길 95 동성로 엔터테인먼트몰 더락\n");
            address.add("대구 중구 동성로2가 70-1 중앙떡볶이 중앙떡볶이 주소가 더 길어야해애애애\n");
            
            ArrayList<String> attract = new ArrayList<>();
            attract.add("더락");
            attract.add("중앙떡볶이");
            
            data.setAddress(address);
            data.setPlaceName(attract);
            
        
            mSubScheduleList.add(data);
        }
        
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.deleteBtn:
                dialogListener.dialogEventListener();
                dismiss();
                break;
        }
    }
    
}
