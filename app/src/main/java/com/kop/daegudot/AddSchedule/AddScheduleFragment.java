package com.kop.daegudot.AddSchedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.applikeysolutions.cosmocalendar.utils.*;
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
//import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.MainActivity;
import com.kop.daegudot.MySchedule.MyScheduleFragment;
import com.kop.daegudot.R;

import java.util.Calendar;
import java.util.List;

public class AddScheduleFragment extends Fragment implements View.OnClickListener {
    View view;
    CalendarView mCalendar;
    Button mCalendarBtn;
    String mFirstDay;
    String mLastDay;

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
                int startDay = startCal.get(Calendar.DAY_OF_MONTH);
                int startMonth = startCal.get(Calendar.MONTH);
                int startYear = startCal.get(Calendar.YEAR);
                String startDate = (startMonth + 1) + "월" + startDay + "일";
                mFirstDay = startYear + "." + (startMonth + 1) + "." + startDay;

                Calendar endCal = days.get(days.size() - 1);
                int endDay = endCal.get(Calendar.DAY_OF_MONTH);
                int endMonth = endCal.get(Calendar.MONTH);
                int endYear = endCal.get(Calendar.YEAR);
                String endDate = (endMonth + 1) + "월" + endDay + "일";
                mLastDay = endYear + "." + (endMonth + 1) + "." + endDay;

                String text = null;
                if (startDate.equals(endDate)) {
                    text = startDate + " - ";
                } else {
                    text = startDate + " - " + endDate;
                }
                mCalendarBtn.setText(text);
             }
        }));



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