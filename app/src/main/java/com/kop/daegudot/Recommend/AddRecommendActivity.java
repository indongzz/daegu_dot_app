package com.kop.daegudot.Recommend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.kop.daegudot.MySchedule.MainScheduleInfo;
import com.kop.daegudot.MySchedule.MyScheduleFragment;
import com.kop.daegudot.Network.Recommend.Hashtag.HashtagResponse;
import com.kop.daegudot.Network.Recommend.RecommendRegister;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.MainScheduleResponse;
import com.kop.daegudot.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 *  추천 글 작성. 및 수정
 */
public class AddRecommendActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddRecommendActivity";
    
    ImageButton backBtn;
    Button mAddMyScheduleBtn;
    EditText mRecommendTitle;
    EditText mRecommendContent;
    ChipGroup mChipGroup;
    RatingBar mRatingBar;
    Button mConfirmBtn;
    
    ArrayList<HashtagResponse> mHashtags;
    private ArrayList<MainScheduleInfo> mMainScheduleList = new ArrayList<>();
    int position = -1;
    List<Integer> mCheckedChipGroup;
    
    /* Rx java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    SharedPreferences pref;
    private String mToken;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recommend);
    
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        
        TextView title = findViewById(R.id.title);
        title.setText("추천 작성");
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        
        // View 설정
        mAddMyScheduleBtn = findViewById(R.id.add_mysch_btn);
        mAddMyScheduleBtn.setOnClickListener(this);
        mRecommendTitle = findViewById(R.id.et_title);
        mRecommendContent = findViewById(R.id.et_content);
        mRatingBar = findViewById(R.id.rating_bar);
        mConfirmBtn = findViewById(R.id.confirm_btn);
        mConfirmBtn.setOnClickListener(this);
        
        mHashtags = RecommendFragment.getHashtags();
        setChipGroup();
    }
    
    public void setChipGroup() {    // 여러 개 선택할 수 있는 chip group
        mChipGroup = findViewById(R.id.chip_group);
        
        Chip[] chips = new Chip[mHashtags.size()];
        
        for(int i = 0; i < mHashtags.size(); i++) {
            chips[i] = (Chip)getLayoutInflater()
                    .inflate(R.layout.layout_chip_choice, mChipGroup, false);
            chips[i].setText(mHashtags.get(i).content);
            chips[i].setId(i);
            
            mChipGroup.addView(chips[i]);
        }
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            finish();
        } else if (v.getId() == R.id.add_mysch_btn) {
            mMainScheduleList = MyScheduleFragment.getMainSchedules();
            
            selectMainScheduleDialog();
        } else if (v.getId() == R.id.confirm_btn) {
            if (checkIfSelectedAll()) {
                RecommendRegister recommendRegister = new RecommendRegister();
                recommendRegister.mainScheduleId =  mMainScheduleList.get(position).getMainId();
                recommendRegister.star = mRatingBar.getRating();
                recommendRegister.title = mRecommendTitle.getText().toString();
                recommendRegister.content = mRecommendContent.getText().toString();
                
                ArrayList<Long> hashtagIds = new ArrayList<>();
                for (int i = 0; i < mCheckedChipGroup.size(); i++) {
                    hashtagIds.add(mHashtags.get(mCheckedChipGroup.get(i)).id);
                }
                
                recommendRegister.hashtagId = hashtagIds;
                
                registerRecommendScheduleRx(recommendRegister);
                
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "값을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    public void selectMainScheduleDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(AddRecommendActivity.this, R.style.AlertDialogStyle);
    
        final int[] checkedItem = {-1};
        String[] list = new String[mMainScheduleList.size()];
        
        int index = 0;
        for (int i = 0; i < mMainScheduleList.size(); i++) {
            Log.d("TIME", mMainScheduleList.get(i).getmEndDate());
            LocalDate date = LocalDate.parse(mMainScheduleList.get(i).getmEndDate());
            if (date.isAfter(LocalDate.now())) {
                list[index++] = mMainScheduleList.get(i).getDateString();
            }
        }
        
        if (index > 0) {
            list = Arrays.copyOf(list, index);
    
            builder.setTitle("날짜를 선택하세요");
            builder.setSingleChoiceItems(list, checkedItem[0], (dialog, which) -> checkedItem[0] = which);
            builder.setPositiveButton("선택", (dialog, which) -> {
                position = checkedItem[0];
        
                mAddMyScheduleBtn.setText(mMainScheduleList.get(position).getDateString());
                dialog.dismiss();
            });
            builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
        else {
            Toast.makeText(getApplicationContext(), "등록 가능한 일정이 없습니다", Toast.LENGTH_SHORT).show();
        }
    }
    
    public boolean checkIfSelectedAll() {
        boolean bool = false;
        
        mCheckedChipGroup = mChipGroup.getCheckedChipIds();
        
        if (!mAddMyScheduleBtn.getText().equals("일정 등록 +")
                && mCheckedChipGroup.size() != 0 && mRatingBar.getRating() != 0) {
            bool = true;
        }
        
        return bool;
    }
    
    private void registerRecommendScheduleRx(RecommendRegister recommendRegister) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<Long> observable = service.registerRecommendSchedule(recommendRegister);
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.d("RX " + TAG, "Next");
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");
                        
                    }
                })
        );
    }
    
}