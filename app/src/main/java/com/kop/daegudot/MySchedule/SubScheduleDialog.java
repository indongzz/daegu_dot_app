package com.kop.daegudot.MySchedule;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.KakaoMap.PlaceBottomSheet;
import com.kop.daegudot.KakaoMap.ViewPagerAdapter;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.SubSchedule;
import com.kop.daegudot.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SubScheduleDialog extends Dialog implements View.OnClickListener {   // 세부 일정
    private static final String TAG = "SubScheduleDialog";
    private Context mContext;
    private RecyclerView recyclerView;
    private SubScheduleAdapter adapter;
    Date[] date;
    MainScheduleInfo mMainScheduleInfo;
    
    private ArrayList<SubScheduleInfo> mSubScheduleList = new ArrayList<>();
    private SubScheduleDialogListener dialogListener;
    
    /* Rxjava*/
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    
    public SubScheduleDialog(@NonNull Context context, MainScheduleInfo mainScheduleInfo,
                             SubScheduleDialogListener dialogListener) {
        super(context);
        this.mContext = context;
        mMainScheduleInfo = mainScheduleInfo;
        this.dialogListener = dialogListener;
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
        
        
        setContentView(R.layout.itinerary_dialog);
        
        TextView dialogTitle = findViewById(R.id.dialogTitle);
        String title = mMainScheduleInfo.getButtonString();
        dialogTitle.setText(title);
    
        Button deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(this);
        
        setItineraryText();
    
        recyclerView = findViewById(R.id.itineraryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    
        adapter = new SubScheduleAdapter(getContext(), mSubScheduleList, mMainScheduleInfo);
        recyclerView.setAdapter(adapter);
    }
    
    public void setItineraryText() {
        /* 세부 일정 날짜 */
    
        getSubScheduleRx(mMainScheduleInfo.getMainId());
        
        
    }
    
    // get subschedules by main schedule id
    private void getSubScheduleRx(long mainScheduleId) {
        Log.d("RX " + TAG, "Start!!!!!!!!!!!!");
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
        Observable<List<SubSchedule>> observable = service.getSubscheduleList(mainScheduleId);
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<SubSchedule>>() {
                    @Override
                    public void onNext(List<SubSchedule> response) {
                        Log.d("RX " + TAG, "getsubschedule: " + "Next");
                        if (response.size() == 0) {
                            /* no subschedule maded need to make subschedule arraylist */
                            
                            LocalDate[] dateArray = mMainScheduleInfo.getDateArray();
                            
                            for (int i = 0; i < mMainScheduleInfo.getDateBetween(); i++) {
                                SubScheduleInfo s = new SubScheduleInfo();
                                String dateText = dateArray[i].toString();
                                s.setDate(dateText);
    
                                ArrayList<String> address = new ArrayList<>();
                                ArrayList<String> attract = new ArrayList<>();
    
                                s.setAddress(address);
                                s.setPlaceName(attract);
    
                                mSubScheduleList.add(s);
                            }
                            
                            Intent intent = new Intent(mContext, MapMainActivity.class);
                            intent.putExtra("MainSchedule", mMainScheduleInfo);
                            intent.putParcelableArrayListExtra("SubScheduleList", mSubScheduleList);
                            intent.putExtra("mainId", mainScheduleId);
                            mContext.startActivity(intent);
                            
                        } else {
                            
                            for (int i = 0; i < response.size(); i++) {
                                SubScheduleInfo s = new SubScheduleInfo();
                                s.setSubId(response.get(i).subId);
                                s.setPlaceNum(response.get(i).placeId);
                                s.setDate(response.get(i).date);
                                
                                ArrayList<String> address = new ArrayList<>();
                                ArrayList<String> attract = new ArrayList<>();

                                s.setAddress(address);
                                s.setPlaceName(attract);
                                
                                mSubScheduleList.add(s);
                            }
                        }
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, "getsubschedule: " + e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "getsubschedule: complete");
                        
                    }
                })
        );
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
