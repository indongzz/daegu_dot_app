package com.kop.daegudot.MySchedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.TextView;

import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.MainScheduleResponse;
import com.kop.daegudot.Network.Schedule.MainScheduleResponseList;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.Collections;

import com.kop.daegudot.MainActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyScheduleFragment extends Fragment implements View.OnClickListener {
    static String TAG = "MyScheduleFragment";
    View view;
    private static ArrayList<MainScheduleInfo> mList;
    private RecyclerView recyclerView;
    private MainScheduleAdapter adapter;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private SharedPreferences pref;
    private String mToken;
    
    public MyScheduleFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        
        selectAllMainScheduleRx();
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_schedule, container, false);
        
        TextView title = view.findViewById(R.id.title);
        title.setText("내 일정");
        
        ImageButton backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        
        recyclerView = view.findViewById(R.id.dateList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        Button addOther = view.findViewById(R.id.addOtherSBtn);
        addOther.setOnClickListener(this);
        
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
        
        return view;
    }
    
    private void selectAllMainScheduleRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<MainScheduleResponseList> observable = service.getMainSchedule();
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MainScheduleResponseList>() {
                    @Override
                    public void onNext(MainScheduleResponseList response) {
                        Log.d("RX MySchedule", "Next");
                        mList = new ArrayList<>();
                        if (response != null) {
                            if (response.status == 1L) {
                                int n = response.mainScheduleResponseDtoArrayList.size();
                                for (int i = 0; i < n; i++) {
                                    MainScheduleResponse m = response.mainScheduleResponseDtoArrayList.get(i);
                                    MainScheduleInfo data = new MainScheduleInfo();
                                    data.setmStartDate(m.startDate);
                                    data.setmEndDate(m.endDate);
                                    data.setmDDate();
                                    Log.d("RX myschedule", "id: " + m.id +
                                            " start: " + m.startDate);
                                    data.setMainId(m.id);
                                    mList.add(data);
                                }
                            } else if (response.status == 0L) {
                                Log.d("Rx MySchedule", "MainSchedule List is null");
                            }
                        }
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX MySchedule", e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX MySchedule", "complete");
                        
                        Collections.sort(mList);
                        updateUI();
                    }
                })
        );
    }
    
    public void updateUI() {
        try {
            adapter = new MainScheduleAdapter(getContext(), mList);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public static void addMainSchedule(MainScheduleInfo mainScheduleInfo) {
        mList.add(mainScheduleInfo);
    }
    
    public static ArrayList<MainScheduleInfo> getMainSchedules() {
        return mList;
    }
    
    public void refresh() {
        // 데이터 추가 시 갱신
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addOtherSBtn) {
            ((MainActivity) getActivity()).changeFragment(0, 1);
        }
        if (v.getId() == R.id.backBtn) {
            ((MainActivity) getActivity()).changeFragment(0, 3);
        }
    }
}