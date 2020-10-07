package com.kop.daegudot.MySchedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;

import com.kop.daegudot.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MyScheduleFragment extends Fragment {
    View view;
    private ArrayList<DateInfo> mList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DateAdapter adapter;
    
    public MyScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_schedule, container, false);

        TextView title = view.findViewById(R.id.title);
        title.setText("내 일정");
        
        // TODO: get date data from DB
        DateInfo data = new DateInfo();
        String firstdate = "20.11.14";
        String lastDate = "20.11.16";
        data.setFirstDate(firstdate);
        data.setLastDate(lastDate);
        data.setdDate(getDDay(firstdate, lastDate));
    
    
        DateInfo data2 = new DateInfo();
        String firstdate2 = "20.10.14";
        String lastDate2 = "20.10.16";
        data2.setFirstDate(firstdate2);
        data2.setLastDate(lastDate2);
        data2.setdDate(getDDay(firstdate2, lastDate2));
    
        mList.add(data);
        mList.add(data2);
        
        Collections.sort(mList, new Comparator<DateInfo>() {
            @Override
            public int compare(DateInfo o1, DateInfo o2) {
                return o1.getdDate().compareTo(o2.getdDate());
            }
        });
        
        recyclerView = view.findViewById(R.id.dateList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new DateAdapter(getContext(), mList);
        recyclerView.setAdapter(adapter);
        
    //    adapter.notifyDataSetChanged();
        
        return view;
    }
    
    public String getDDay(String first, String last) {
        String format = "yy.MM.dd";
        String dday = null;
    
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            Date startDate = dateFormat.parse(first);
            System.out.println(startDate);
            Date today = Calendar.getInstance().getTime();
            System.out.println(today);
            long diff = (startDate.getTime() - today.getTime()) / (24 * 60 * 60 * 1000);
            System.out.println(diff + "일");
            dday = String.valueOf(diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return dday;
    }

}