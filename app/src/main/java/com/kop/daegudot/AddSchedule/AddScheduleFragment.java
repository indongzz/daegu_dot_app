package com.kop.daegudot.AddSchedule;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.applikeysolutions.cosmocalendar.utils.*;
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
//import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.MainActivity;
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.MyScheduleFragment;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.MainScheduleRegister;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 *  Second Fragment' 일정추가
 *
 *  select two dates to make main schedule
 *  and add to database
 */

public class AddScheduleFragment extends Fragment implements View.OnClickListener {
    final static private String TAG = "AddScheduleFragment";
    View view;
    CalendarView mCalendar;
    Button mCalendarBtn;
    String mStartDate, mEndDate;
    String mBtnDay1, mBtnDay2;
    ProgressBar mProgressBar;
    LocalDate localStart, localEnd;
    int flag = 1;
    FragmentManager mFragmentManager;
    
    private SharedPreferences pref;
    private String mToken;
    UserResponse user;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    long mMainId;

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
    
        mProgressBar = view.findViewById(R.id.progress_bar);
    
        pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        
        mFragmentManager = getFragmentManager();
        
        // 토큰으로 사용자 정보 가져오기
        selectUserByToken();
        
        // change top title
        TextView title = view.findViewById(R.id.title);
        title.setText("일정 추가");
        ImageButton backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        
        mCalendar = view.findViewById(R.id.calendar);
        mCalendarBtn = view.findViewById(R.id.calendarBtn);

        mCalendarBtn.setOnClickListener(this);

        mCalendar.setSelectionType(SelectionType.RANGE);
    
        Set<Long> disabledDaysSet = new HashSet<>();
    
        long date = System.currentTimeMillis();
        long mul = 0;
        
        // 100일 전까지 선택 불가
        for (int i = 1; i <= 100; i++) {
            mul += 86400000;
            disabledDaysSet.add(date - mul);
        }
        
        mCalendar.setDisabledDays(disabledDaysSet);
        
        mProgressBar.setVisibility(View.GONE);

        mCalendar.setSelectionManager(new RangeSelectionManager(new OnDaySelectedListener() {
            @Override
            public void onDaySelected() {
                List<Calendar> days = mCalendar.getSelectedDates();

                Calendar startCal = days.get(0);
                localStart = LocalDateTime.ofInstant(
                        startCal.toInstant(), startCal.getTimeZone().toZoneId()).toLocalDate();
                        
                mBtnDay1 = localStart.format(DateTimeFormatter.ofPattern("M월d일"));
                mStartDate = localStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Log.i(TAG, "mStartDate: " + mStartDate + "mbtnday1: " + mBtnDay1);
                
                Calendar endCal = days.get(days.size() - 1);
                localEnd = LocalDateTime.ofInstant(
                        endCal.toInstant(), endCal.getTimeZone().toZoneId()).toLocalDate();

                mBtnDay2 = localEnd.format(DateTimeFormatter.ofPattern("M월d일"));
                mEndDate = localEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

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

         return view;
    }

    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.calendarBtn) {
            if (flag == 1) {
                mProgressBar.setVisibility(View.VISIBLE);
                MainScheduleRegister mainScheduleRegister = new MainScheduleRegister();
                mainScheduleRegister.startDate = localStart.toString();
                mainScheduleRegister.endDate = localEnd.toString();
                mainScheduleRegister.userId = user.id;
                registerMainSchedule(mainScheduleRegister);
            } else {
                Toast.makeText(getContext(), "날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.backBtn) {
                ((MainActivity)getActivity()).changeFragment(1, 0);
        }
    }
    
   private void registerMainSchedule(MainScheduleRegister mainSchedule) {
       RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
       
       Observable<Long> observable = service.saveMainSchedule(mainSchedule);
       
       mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(new DisposableObserver<Long>() {
                   @Override
                   public void onNext(Long response) {
                       Log.d("RX AddScheduleFragment", "Next" + " Response id:: " + response);
                       mMainId = response;
                       
                       MainScheduleInfo mainScheduleInfo = new MainScheduleInfo();
                       mainScheduleInfo.setMainId(mMainId);
                       mainScheduleInfo.setmStartDate(mainSchedule.startDate);
                       mainScheduleInfo.setmEndDate(mainSchedule.endDate);
                       mainScheduleInfo.setmDDate();
                       
                       MyScheduleFragment.addMainSchedule(mainScheduleInfo);
                   }
                
                   @Override
                   public void onError(Throwable e) {
                       Log.d("RX AddScheduleFragment", e.getMessage());
                       Toast.makeText(getContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                   }
                
                   @Override
                   public void onComplete() {
                       Log.d("RX AddScheduleFragment", "complete");
                       
                       MainScheduleInfo mainScheduleInfo = new MainScheduleInfo();
                       mainScheduleInfo.setmStartDate(mStartDate);
                       mainScheduleInfo.setmEndDate(mEndDate);
                       mainScheduleInfo.setMainId(mMainId);
                       mainScheduleInfo.setmDDate();
    
                       mProgressBar.setVisibility(View.GONE);
                       
                       Intent intent = new Intent(getContext(), MapMainActivity.class);
                       intent.putExtra("mainSchedule", mainScheduleInfo);
                       startActivity(intent);
                       flag = 0;
                       
                   }
               })
       );
   }
    
    //토큰으로 회원 정보 가져오기
    private void selectUserByToken() {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(mToken);
        Observable<UserResponse> observable = service.getUserFromToken();
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        user = response;
                        Log.d("TOKEN", "TOKEN OK" + " " + response.email);
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("TOKEN", e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("TOKEN", "complete");
                    }
                })
        );
    }
}