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
import com.kop.daegudot.Network.Recommend.Hashtag.HashtagResponseList;
import com.kop.daegudot.Network.Recommend.RecommendRegister;
import com.kop.daegudot.Network.Recommend.RecommendResponse;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.MainScheduleResponse;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class UpdateRecommendActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "UpdateRecommendActivity";
    ImageButton backBtn;
    Button mUpdateScheduleBtn;
    EditText mRecommendTitle;
    EditText mRecommendContent;
    ChipGroup mChipGroup;
    RatingBar mRatingBar;
    Button mConfirmBtn;
    
    ArrayList<HashtagResponse> mHashtags;
    private ArrayList<MainScheduleInfo> mMainScheduleList = new ArrayList<>();
    int mainPosition = -1;
    List<Integer> mCheckedChipGroup;
    int listIndex = 0;
    
    /* Rx java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    SharedPreferences pref;
    private String mToken;
    RecommendResponse mRecommendPost;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recommend);
    
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
    
        TextView title = findViewById(R.id.title);
        title.setText("추천 작성");
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
    
        // View 설정
        mUpdateScheduleBtn = findViewById(R.id.update_mysch_btn);
        mUpdateScheduleBtn.setOnClickListener(this);
        mRecommendTitle = findViewById(R.id.et_title);
        mRecommendContent = findViewById(R.id.et_content);
        mRatingBar = findViewById(R.id.rating_bar);
        mChipGroup = findViewById(R.id.chip_group);
        mConfirmBtn = findViewById(R.id.confirm_btn);
        mConfirmBtn.setOnClickListener(this);
        
        mHashtags = RecommendFragment.getHashtags();
        if (mHashtags == null) {
            selectHashtagsRx();
        } else {
            setChipGroup();
            getPreviousContent();
        }
    }
    
    public void getPreviousContent() {
        Intent intent = getIntent();
        if (intent != null) {
            mRecommendPost = intent.getParcelableExtra("recommendPost");
            if (mRecommendPost != null) {
                listIndex = intent.getIntExtra("position", 0);
                MainScheduleResponse main = mRecommendPost.mainScheduleResponseDto;
                String date = main.startDate.substring(5, 7) + "." + main.startDate.substring(8, 10)+ " ~ " +
                        main.endDate.substring(5, 7) + "." +  main.endDate.substring(8, 10);
                
                mMainScheduleList = MyScheduleFragment.getMainSchedules();
                for (int i = 0; i < mMainScheduleList.size(); i++) {
                    if (mMainScheduleList.get(i).getDateString().equals(date)) {
                        mainPosition = i;
                        break;
                    }
                }
                mUpdateScheduleBtn.setText(date);
                for (int i = 0; i < mRecommendPost.hashtags.size(); i++) {
                    mChipGroup.check((int) mRecommendPost.hashtags.get(i).id - 1);
                }
                mRatingBar.setRating(mRecommendPost.getStar());
                mRecommendTitle.setText(mRecommendPost.title);
                mRecommendContent.setText(mRecommendPost.content);
            }
        }
    }
    
    public void setChipGroup() {    // 여러 개 선택할 수 있는 chip group
        
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
                recommendRegister.mainScheduleId =  mMainScheduleList.get(mainPosition).getMainId();
                recommendRegister.star = mRatingBar.getRating();
                recommendRegister.title = mRecommendTitle.getText().toString();
                recommendRegister.content = mRecommendContent.getText().toString();
                
                ArrayList<Long> hashtagIds = new ArrayList<>();
                for (int i = 0; i < mCheckedChipGroup.size(); i++) {
                    hashtagIds.add(mHashtags.get(mCheckedChipGroup.get(i)).id);
                }
                
                recommendRegister.hashtagId = hashtagIds;
              
                updateRecommendScheduleRx(recommendRegister);
                
            }
            else {
                Toast.makeText(getApplicationContext(), "값을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    public void selectMainScheduleDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(UpdateRecommendActivity.this, R.style.AlertDialogStyle);
        
        final int[] checkedItem = {-1};
        String[] list = new String[mMainScheduleList.size()];
        
        for (int i = 0; i < mMainScheduleList.size(); i++) {
            list[i] = mMainScheduleList.get(i).getDateString();
            Log.d(TAG, list[i]);
        }
        
        builder.setTitle("날짜를 선택하세요");
        builder.setSingleChoiceItems(list, checkedItem[0], (dialog, which) -> checkedItem[0] = which);
        builder.setPositiveButton("선택", (dialog, which) -> {
            mainPosition = checkedItem[0];
            
            mUpdateScheduleBtn.setText(mMainScheduleList.get(mainPosition).getDateString());
            dialog.dismiss();
        });
        builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    
    public boolean checkIfSelectedAll() {
        boolean bool = false;
        
        mCheckedChipGroup = mChipGroup.getCheckedChipIds();
        
        if (!mUpdateScheduleBtn.getText().equals("일정 등록 +")
                && mCheckedChipGroup.size() != 0 && mRatingBar.getRating() != 0) {
            bool = true;
        }
        
        return bool;
    }
    
    void updateRecommendScheduleRx(RecommendRegister recommendRegister) {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<Long> observable = service.updateRecommendSchedule(mRecommendPost.id, recommendRegister);
        
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
                        RecommendResponse recommendResponse = makeRecommendObject(recommendRegister);
                        
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedRecommendPost", recommendResponse);
                        resultIntent.putExtra("position", listIndex);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                })
        );
    }
    
    public RecommendResponse makeRecommendObject(RecommendRegister recommendRegister) {
        RecommendResponse recommendResponse = new RecommendResponse();
    
        recommendResponse.id = mRecommendPost.id;
        recommendResponse.title = recommendRegister.title;
        recommendResponse.content = recommendRegister.content;
        recommendResponse.userResponseDto = mRecommendPost.userResponseDto;
        ArrayList<HashtagResponse> hashtags = new ArrayList<>();
        for (int i = 0; i < mCheckedChipGroup.size(); i++) {
            hashtags.add(mHashtags.get(mCheckedChipGroup.get(i)));
        }
        recommendResponse.hashtags = hashtags;
        recommendResponse.star = recommendRegister.star;
        recommendResponse.localDateTime = mRecommendPost.localDateTime;
    
        MainScheduleResponse mainScheduleResponse = new MainScheduleResponse();
        mainScheduleResponse.startDate = mMainScheduleList.get(mainPosition).getmStartDate();
        mainScheduleResponse.endDate = mMainScheduleList.get(mainPosition).getmEndDate();
        mainScheduleResponse.id = mMainScheduleList.get(mainPosition).getMainId();
        recommendResponse.mainScheduleResponseDto = mainScheduleResponse;
    
        return recommendResponse;
    }
    
    private void selectHashtagsRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<HashtagResponseList> observable = service.selectHashtagList();
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<HashtagResponseList>() {
                    @Override
                    public void onNext(HashtagResponseList response) {
                        Log.d("RX " + TAG, "Next");
                        
                        mHashtags = response.hashtagResponseDtoArrayList;
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");
                        setChipGroup();
                        getPreviousContent();
                    }
                })
        );
    }
}