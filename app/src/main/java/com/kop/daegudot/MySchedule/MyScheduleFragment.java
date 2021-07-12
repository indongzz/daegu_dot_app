package com.kop.daegudot.MySchedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.MainSchedule;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kop.daegudot.MainActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyScheduleFragment extends Fragment implements View.OnClickListener {
    View view;
    private ArrayList<MainScheduleInfo> mList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MainScheduleAdapter adapter;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    
    public MyScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        selectAllMainSchedule();
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
        
        Button addOther = view.findViewById(R.id.addOtherSBtn);
        addOther.setOnClickListener(this);
        
        Collections.sort(mList);
        adapter = new MainScheduleAdapter(getContext(), mList);
        recyclerView.setAdapter(adapter);
        Log.d("MyScheduleFragment", "RecyclerView Adapter Done");
        
        return view;
    }
    
    private void selectAllMainSchedule() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<List<MainSchedule>> observable = service.getMainSchedule(1);
    
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<MainSchedule>>() {
                    @Override
                    public void onNext(List<MainSchedule> response) {
                        Log.d("RX", "Next");
                        if (response != null) {
                            for (int i = 0; i < response.size(); i++) {
                                MainSchedule m = response.get(i);
                                MainScheduleInfo data = new MainScheduleInfo();
                                data.setmStartDate(m.startDate);
                                data.setmEndDate(m.endDate);
                                data.setmDDate();
                                // TODO: id 부분 받아오는거 해결되면 수정필요
                                Log.d("RX myschedule", "id: " + m.mainId +
                                        " start: " + m.startDate + " userid & email: " +
                                        +m.user.id + m.user.email);
                                data.setMainId(m.mainId);
                                mList.add(data);
                            }
                        }
                    }
                
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX", e.getMessage());
                    }
                
                    @Override
                    public void onComplete() {
                        Log.d("RX", "complete");
    
                        Collections.sort(mList);
                        refresh();
                    }
                })
        );
    }
    
    
    public void refresh() {
        // 데이터 추가 시 갱신
         adapter.notifyDataSetChanged();
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
    
//    public void ConvertToMapMainActivity() {
//        Intent intent = new Intent(getContext(), MapMainActivity.class);
//        intent.putExtra("firstDay", mFirstDay);
//        intent.putExtra("lastDay", mLastDay);
//
//        startActivity(intent);
//    }
}