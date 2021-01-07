package com.kop.daegudot.Recommend;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kop.daegudot.R;

public class RecommendFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RecommendFragment";
    
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
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        
        TextView title = view.findViewById(R.id.title);
        title.setText("추천");
        
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        
        return view;
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