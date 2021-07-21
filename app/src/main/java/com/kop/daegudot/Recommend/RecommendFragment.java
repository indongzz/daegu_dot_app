package com.kop.daegudot.Recommend;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kop.daegudot.R;

import java.util.Objects;

public class RecommendFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RecommendFragment";
    View mView;
    
    String[] hashtags = {"갬성", "힐링", "맛집", "해시태그1", "해시태그2", "123", "해시", "해시", "해시23"};
    ChipGroup mChipGroup;
    
    ListView mListView;
    RecommendHashtagListAdapter adapter;
    
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
        
        mListView = mView.findViewById(R.id.listview);
        adapter = new RecommendHashtagListAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), RecommendListActivity.class);
                intent.putExtra("hashtag", hashtags[position]);
                startActivity(intent);
            }
        });
        
        setRecommendImageList();
        
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
            chips[i].setId(i);
            chips[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "getid: " + v.getId());
                    mListView.smoothScrollToPositionFromTop(v.getId(), 0, 100);
                }
            });
            mChipGroup.addView(chips[i]);
        }
    }
    
    public void setRecommendImageList() {
        int n = hashtags.length;
        Drawable[] drawables = new Drawable[n];
        drawables[0] = ContextCompat.getDrawable(getActivity(), R.drawable.picex);
        drawables[1] = ContextCompat.getDrawable(getActivity(), R.drawable.pic_eat);
        
        for (int i = 0; i < n; i++) {
            adapter.addItem(drawables[i%2],"#" + hashtags[i]);
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