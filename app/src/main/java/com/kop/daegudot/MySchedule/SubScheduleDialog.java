package com.kop.daegudot.MySchedule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.SubScheduleResponse;
import com.kop.daegudot.Network.Schedule.SubScheduleResponseList;
import com.kop.daegudot.R;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * '내 일정' 탭에서 메인 스케줄 중 하나를 클릭 했을 때 실행되는 Dialog
 * SubSchedule들을 일차별로 띄우고 해당 일차를 클릭 시
 *      MapMainAcitivity로 이동 후 해당 일차의 일정 BottomSheet띄움
 * SubSchedule이 없으면 바로 MapMainActivity로 이동
 */
public class SubScheduleDialog extends Dialog implements View.OnClickListener {   // 세부 일정
    private static final String TAG = "SubScheduleDialog";
    private Context mContext;
    private RecyclerView recyclerView;
    private SubScheduleAdapter adapter;
    MainScheduleInfo mMainScheduleInfo;
    
    private SubScheduleDialogListener dialogListener;
    private ArrayList<SubScheduleResponse> mSubScheduleList;
    
    /* Rxjava*/
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final String mToken;
    SharedPreferences pref;
    
    public SubScheduleDialog(@NonNull Context context, MainScheduleInfo mainScheduleInfo,
                             SubScheduleDialogListener dialogListener) {
        super(context);
        this.mContext = context;
        mMainScheduleInfo = mainScheduleInfo;
        this.dialogListener = dialogListener;
        pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
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
        
        setContentView(R.layout.subschedule_dialog);
        
        TextView dialogTitle = findViewById(R.id.dialogTitle);
        String title = mMainScheduleInfo.getButtonString();
        dialogTitle.setText(title);
    
        Button deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(this);
        
        /* get all subschedule on db */
        selectAllSubScheduleList(mMainScheduleInfo.getMainId());
    
        recyclerView = findViewById(R.id.subScheduleLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
    }
    
    // get subschedules by main schedule id
    private void selectAllSubScheduleList(long mainScheduleId) {
        Log.d("RX " + TAG, "Start!!!!!!!!!!!!");
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<SubScheduleResponseList> observable = service.getSubscheduleList(mainScheduleId);
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SubScheduleResponseList>() {
                    @Override
                    public void onNext(SubScheduleResponseList response) {
                        Log.d("RX " + TAG, "getsubschedule: " + "Next");
                        mSubScheduleList = new ArrayList<>();
                        if (response.status == 0L) {
                            /* no subschedule just pass MapMainSchedule */
                            moveToMapMainActivity();
                        }
                        else if (response.status == 1L){
                            // subschedule exists
                            mSubScheduleList = response.subScheduleResponseDtoArrayList;
                        }
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, "getsubschedule: " + e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "getsubschedule: complete");
                        
                        Collections.sort(mSubScheduleList);
                        
                        adapter = new SubScheduleAdapter(getContext(),
                                getDateSubSchedules(mMainScheduleInfo, mSubScheduleList),
                                mMainScheduleInfo);
                        
                        recyclerView.setAdapter(adapter);
                    }
                })
        );
    }
    
    public static ArrayList<DateSubSchedule> getDateSubSchedules(MainScheduleInfo main, ArrayList<SubScheduleResponse> sub) {
        ArrayList<DateSubSchedule> dateSubSchedules = new ArrayList<>();
        
        LocalDate[] dateArray = main.getDateArray();
        
        for (LocalDate localDate : dateArray) {
            DateSubSchedule ds = new DateSubSchedule();
            ds.date = localDate.toString();
            ds.subScheduleList = new ArrayList<>();
            dateSubSchedules.add(ds);
        }
        
        if (sub != null) {
            int index = 0;
            for (int i = 0; i < sub.size(); i++) {
                if (dateSubSchedules.get(index).date.equals(sub.get(i).date)) {
                    dateSubSchedules.get(index).subScheduleList.add(sub.get(i));
                } else {
                    index++;
                    i--;
                }
            }
        }
        
//        for (int i = 0; i < dateSubSchedules.size(); i++) {
//            Log.d(TAG, dateSubSchedules.get(i).subScheduleList + " ");
//        }
        
        return dateSubSchedules;
    }
    
    public void moveToMapMainActivity() {
        Intent intent = new Intent(mContext, MapMainActivity.class);
        intent.putExtra("mainSchedule", mMainScheduleInfo);
        mContext.startActivity(intent);
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
