package com.kop.daegudot.AddSchedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.applikeysolutions.cosmocalendar.utils.*;
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
//import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.R;

import com.kop.daegudot.MainActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class AddScheduleFragment extends Fragment implements View.OnClickListener {
    final static private String TAG = "AddScheduleFragment";
    View view;
    CalendarView mCalendar;
    Button mCalendarBtn;
    String mStartDate, mEndDate;
    String mBtnDay1, mBtnDay2;
     int flag = 1;

    public AddScheduleFragment() {
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
        view = inflater.inflate(R.layout.fragment_add_schedule, container, false);

        // change top title
        TextView title = view.findViewById(R.id.title);
        title.setText("일정 추가");

        mCalendar = view.findViewById(R.id.calendar);
        mCalendarBtn = view.findViewById(R.id.calendarBtn);

        mCalendarBtn.setOnClickListener(this);

        mCalendar.setSelectionType(SelectionType.RANGE);

        mCalendar.setSelectionManager(new RangeSelectionManager(new OnDaySelectedListener() {
            @Override
            public void onDaySelected() {
                List<Calendar> days = mCalendar.getSelectedDates();

                Calendar startCal = days.get(0);
                LocalDate localStart = LocalDateTime.ofInstant(
                        startCal.toInstant(), startCal.getTimeZone().toZoneId()).toLocalDate();
                        
                mBtnDay1 = localStart.format(DateTimeFormatter.ofPattern("M월d일"));
                mStartDate = localStart.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
                Log.i(TAG, "mStartDate: " + mStartDate + "mbtnday1: " + mBtnDay1);
                
                Calendar endCal = days.get(days.size() - 1);
                LocalDate localEnd = LocalDateTime.ofInstant(
                        endCal.toInstant(), endCal.getTimeZone().toZoneId()).toLocalDate();

                mBtnDay2 = localEnd.format(DateTimeFormatter.ofPattern("M월d일"));
                mEndDate = localEnd.format(DateTimeFormatter.ofPattern("yy.MM.dd"));

                String text = null;
                if (mBtnDay1.equals(mBtnDay2)) {
                    text = mBtnDay1 + " - ";
                } else {
                    text = mBtnDay1 + " - " + mBtnDay2;
                }
                mCalendarBtn.setText(text);
                
                if (text.length() > 10)
                    flag = 1;
                else flag = 0;
    
             }
        }));
        
        mCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (flag == 1) {
                    // change to Kakao Map Activity
                    Intent intent = new Intent(getContext(), MapMainActivity.class);
                    intent.putExtra("startDay", mStartDate);
                    intent.putExtra("endDay", mEndDate);
                    startActivity(intent);
                    flag = 0;
                } else {
                    Toast.makeText(getContext(), "날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

         return view;
    }

    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.calendarBtn:
                // TODO: DB에 MainSchedule 추가
                ((MainActivity)getActivity()).changeFragment(1, 0);
        }
    }
}