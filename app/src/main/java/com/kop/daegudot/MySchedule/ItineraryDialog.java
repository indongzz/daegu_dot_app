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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItineraryDialog extends Dialog {
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
        
        setAscendingDate(firstDay, lastDay);
    
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");
        
        for (int i = 0; i < 3; i++) {
            ItineraryInfo data = new ItineraryInfo();
            String dateText = i + 1 + "일차 - " + dateFormat.format(date[i]);
            
            data.setDate(dateText);
            
            mDay.add(data);
        }
    
        recyclerView = findViewById(R.id.itineraryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    
        adapter = new ItineraryAdapter(getContext(), mDay);
        recyclerView.setAdapter(adapter);
    }
    
    public void setAscendingDate(String first, String last) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd");
        
        try {
            Date firstDate = sdf.parse(first);
            Date lastDate = sdf.parse(last);
            long diff = (lastDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000);
            date = new Date[(int) diff + 1];
            for (int i = 0; i <= diff; i++) {
                long cal = firstDate.getTime() + i * (24 * 60 * 60 * 1000);
                date[i] = new Date();
                date[i].setTime(cal);
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
    }
}
