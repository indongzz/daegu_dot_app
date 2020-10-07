package com.kop.daegudot.MySchedule;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.R;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ItineraryDialog extends Dialog {   // 세부 일정
    private Context mContext;
    private RecyclerView recyclerView;
    private ItineraryAdapter adapter;
    Bundle dateData;
    String firstDay, lastDay;
    Date[] date;
    
    private ArrayList<ItineraryInfo> mDay = new ArrayList<>();
    
    public ItineraryDialog(@NonNull Context context, Bundle data) {
        super(context);
        this.mContext = context;
        dateData = data;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        
        setContentView(R.layout.itinerary_dialog);
        
        firstDay = dateData.getString("first");
        lastDay = dateData.getString("last");
        
        TextView dialogTitle = findViewById(R.id.dialogTitle);
        String title = firstDay.substring(3, 8) + " ~ " + lastDay.substring(3, 8);
        dialogTitle.setText(title);
        
        /* 세부 일정 날짜 */
        int num = setAscendingDate(firstDay, lastDay);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");
        for (int i = 0; i < num; i++) {
            // TODO: 그 날짜 세부 일정 불러오기
            ItineraryInfo data = new ItineraryInfo();
            String dateText = i + 1 + "일차 - " + dateFormat.format(date[i]);
            
            data.setDate(dateText);
            
            ArrayList<String> address = new ArrayList<>();
            
            address.add("대구 중구 동성로2길 95 동성로 엔터테인먼트몰 더락\n");
            address.add("대구 중구 동성로2가 70-1 중앙떡볶이 중앙떡볶이 주소가 더 길어야해애애애\n");
            data.setAddress(address);
            
            mDay.add(data);
        }
    
        recyclerView = findViewById(R.id.itineraryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    
        adapter = new ItineraryAdapter(getContext(), mDay);
        recyclerView.setAdapter(adapter);
    }
    
    public int setAscendingDate(String first, String last) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd");
        int count = 0;
        try {
            Date firstDate = sdf.parse(first);
            Date lastDate = sdf.parse(last);
            long diff = (lastDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000);
            count = (int)diff + 1;
            date = new Date[count];
            for (int i = 0; i < count; i++) {
                long cal = firstDate.getTime() + i * (24 * 60 * 60 * 1000);
                date[i] = new Date();
                date[i].setTime(cal);
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return count;
    }
}
