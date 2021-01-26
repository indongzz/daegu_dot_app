package com.kop.daegudot.Recommend;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kop.daegudot.R;

public class RecommendFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RecommendFragment";
    View mView;
    
    String[] hashtags = {"갬성", "힐링", "맛집", "해시태그1", "해시태그2", "123", "해시", "해시", "해시23"};
    ChipGroup mChipGroup;
    
    ListView mListView;
    
    public RecommendFragment() {
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
        mView = inflater.inflate(R.layout.fragment_recommend, container, false);
        
        
        TextView title = mView.findViewById(R.id.title);
        title.setText("추천");
    
        // hashtag buttons setting
        mChipGroup = mView.findViewById(R.id.btns_group);
        setHashtagBtns();
        
        mListView = mView.findViewById(R.id.list_recommend);
        
        // 우측 하단 글 작성 버튼
        FloatingActionButton fab = mView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        
        
        return mView;
    }
    
    public void setHashtagBtns() {
        int n = hashtags.length;
        
        final Chip[] chips = new Chip[n];
        
        for(int i = 0; i < n; i++) {
            chips[i] = (Chip)getLayoutInflater()
                    .inflate(R.layout.layout_chip_hash1, mChipGroup, false);
            chips[i].setText(hashtags[i]);
            chips[i].setTag(hashtags[i]);
            chips[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(getContext(), RecommendListActivity.class);
                    Log.i(TAG, "hashtag: " + v.getTag().toString());
                    intent2.putExtra("hashtag", v.getTag().toString());
                    startActivity(intent2);
                }
            });
            mChipGroup.addView(chips[i]);
        }
    
    

    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(getContext(), AddRecommendActivity.class);
                startActivity(intent);
                break;
        }
    }
    
}