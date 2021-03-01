package com.kop.daegudot.Recommend;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class PostScheduleBottomSheetDialog extends BottomSheetDialogFragment {
    public final static String TAG = "PostScheduleBottomSheetDialog";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ArrayList<PostScheduleItem> mScheduleList;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_post_schedule, container,false);
        mContext = getActivity();
    
        prepare();
        
        mRecyclerView = view.findViewById(R.id.schedule_recyclerview);
        PostScheduleAdapter adapter = new PostScheduleAdapter(mContext, mScheduleList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        
        return view;
    }
    
    public void prepare() {
        mScheduleList = new ArrayList<>();
        
        ArrayList<String> test = new ArrayList<>();
        test.add("더락");
        test.add("동성로");
        test.add("명소명소");
        test.add("카페");
        test.add("ㅁ여소명소");
        
        ArrayList<String> test2 = new ArrayList<>();
        test2.add("명소명소");
        test2.add("카페");
        test2.add("ㅁ여소명소");
        
        ArrayList<String> test3 = new ArrayList<>();
        test3.add("명소명소");
        test3.add("카페");
        
        PostScheduleItem data = new PostScheduleItem();
        data.setDay(1);
        data.setPlaceName(test);
        
        mScheduleList.add(data);
        
        PostScheduleItem data2 = new PostScheduleItem();
        data2.setDay(2);
        data2.setPlaceName(test2);
        
        mScheduleList.add(data2);
        
        PostScheduleItem data3 = new PostScheduleItem();
        data3.setDay(3);
        data3.setPlaceName(test3);
        
        mScheduleList.add(data3);
    }
}
