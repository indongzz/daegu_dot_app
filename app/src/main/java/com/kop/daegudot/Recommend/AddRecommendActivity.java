package com.kop.daegudot.Recommend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.kop.daegudot.R;

public class AddRecommendActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddRecommendActivity";
    
    ImageButton backBtn;
    Button mAddMyScheduleBtn;
    EditText mRecommendTitle;
    EditText mRecommendContent;
    ChipGroup mChipGroup;
    RatingBar mRatingBar;
    Button mConfirmBtn;
    
    // TODO: 해시태그 리스트 변경
    String[] hashs = {"해시태그1", "해시태그2", "해시태그3", "해시태그4"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recommend);
        
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
        
        setChipGroup();
    }
    
    public void setChipGroup() {    // 여러 개 선택할 수 있는 chip group
        mChipGroup = findViewById(R.id.chip_group);
        
        Chip[] chips = new Chip[hashs.length];
        
        for(int i = 0; i < hashs.length; i++) {
            chips[i] = (Chip)getLayoutInflater()
                    .inflate(R.layout.layout_chip_choice, mChipGroup, false);
            chips[i].setText(hashs[i]);
            
            mChipGroup.addView(chips[i]);
        }
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.add_mysch_btn:
                // TODO: 내 일정 list 가져와서 선택하기
                
                break;
            case R.id.confirm_btn:
                // TODO: 버튼 확인 후 내용 모두 저장
                Log.i(TAG, "chip group id: " + mChipGroup.getCheckedChipIds());
                Log.i(TAG, "rating bar" + mRatingBar.getRating());
                
                finish();
                break;
        }
    }
}