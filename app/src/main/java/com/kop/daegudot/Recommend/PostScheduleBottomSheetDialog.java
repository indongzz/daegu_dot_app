package com.kop.daegudot.Recommend;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kop.daegudot.MySchedule.DateSubSchedule;
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.SubScheduleDialog;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.MainScheduleResponse;
import com.kop.daegudot.Network.Schedule.SubScheduleResponse;
import com.kop.daegudot.Network.Schedule.SubScheduleResponseList;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PostScheduleBottomSheetDialog extends BottomSheetDialogFragment {
    public final static String TAG = "PostScheduleBottomSheetDialog";
    private Context mContext;
    private RecyclerView mRecyclerView;
    PostScheduleAdapter mAdapter;
    private MainScheduleResponse mMainScheduleResponse;
    private ArrayList<SubScheduleResponse> mSubScheduleList;
    private ArrayList<DateSubSchedule> mDateSubScheduleList;
    
    /* Rx java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private String mToken;
    SharedPreferences pref;
    
    public PostScheduleBottomSheetDialog(MainScheduleResponse mainSchedule) {
        mMainScheduleResponse = mainSchedule;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_post_schedule, container,false);
        mContext = getActivity();
        
        pref = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        
        selectAllSubScheduleList(mMainScheduleResponse.id);
        
        mRecyclerView = view.findViewById(R.id.schedule_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        
        return view;
    }
    
    // get subschedules by main schedule id
    private void selectAllSubScheduleList(long mainScheduleId) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<SubScheduleResponseList> observable = service.getSubscheduleList(mainScheduleId);
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SubScheduleResponseList>() {
                    @Override
                    public void onNext(SubScheduleResponseList response) {
                        Log.d("RX " + TAG, "getsubschedule: " + "Next");
                        if (response.status == 0L) {
                            /* no subschedule just pass MapMainSchedule */
                            
                        } else if (response.status == 1L){
                            // subschedule exists
                            mSubScheduleList = new ArrayList<>();
                            mSubScheduleList.addAll(response.subScheduleResponseDtoArrayList);
                        }
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, "getsubschedule: " + e.getMessage());
                        // TODO: select SubSchedule 수정
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "getsubschedule: complete");
    
                        Collections.sort(mSubScheduleList);
                        getDateSubSchedule();
                        
                        updateUI();
                    }
                })
        );
    }
    
    public void getDateSubSchedule() {
        MainScheduleInfo mainScheduleInfo = new MainScheduleInfo();
        mainScheduleInfo.setMainId(mMainScheduleResponse.id);
        mainScheduleInfo.setmStartDate(mMainScheduleResponse.startDate);
        mainScheduleInfo.setmEndDate(mMainScheduleResponse.endDate);
        mainScheduleInfo.setmDDate();
        
        mDateSubScheduleList = SubScheduleDialog.getDateSubSchedules(mainScheduleInfo, mSubScheduleList);
    }
    
    public void updateUI() {
        mAdapter = new PostScheduleAdapter(mContext, mDateSubScheduleList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
