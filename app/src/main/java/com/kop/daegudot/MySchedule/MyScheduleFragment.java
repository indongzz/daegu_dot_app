package com.kop.daegudot.MySchedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kop.daegudot.MainActivity;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.Collections;


public class MyScheduleFragment extends Fragment implements View.OnClickListener {
    View view;
    private ArrayList<MainScheduleInfo> mList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MainScheduleAdapter adapter;
    
    public MyScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_schedule, container, false);

        TextView title = view.findViewById(R.id.title);
        title.setText("내 일정");
        
        recyclerView = view.findViewById(R.id.dateList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        adapter = new MainScheduleAdapter(getContext(), mList);
        recyclerView.setAdapter(adapter);
        
        Button addOther = view.findViewById(R.id.addOtherSBtn);
        addOther.setOnClickListener(this);
    
        return view;
    }
    
    private void prepareData() {
        // TODO: get date data from DB
        MainScheduleInfo data = new MainScheduleInfo();
        String firstdate = "20.11.14";
        String lastDate = "20.11.16";
        data.setmFirstDate(firstdate);
        data.setmLastDate(lastDate);
        data.setmDDate();
        
        MainScheduleInfo data2 = new MainScheduleInfo();
        String firstdate2 = "20.12.09";
        String lastDate2 = "20.12.16";
        data2.setmFirstDate(firstdate2);
        data2.setmLastDate(lastDate2);
        data2.setmDDate();

        mList.add(data);
        mList.add(data2);
    
        Collections.sort(mList);
    }
    
    
    public void refresh() {
        // 데이터 추가 시 갱신
        // adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addOtherSBtn:
                // 0 = MyScheduleFragment, 1 = AddScheduleFragment
                ((MainActivity)getActivity()).changeFragment(0, 1);
                break;
        }
    }
}