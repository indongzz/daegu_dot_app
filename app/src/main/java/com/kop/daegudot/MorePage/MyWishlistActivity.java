package com.kop.daegudot.MorePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kop.daegudot.KakaoMap.MarkerInfo;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class MyWishlistActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MyWishListActivity";
    private Context mContext;
    private ArrayList<MarkerInfo> mWishList;
    private RecyclerView mRecyclerView;
    private WishlistAdapter mWishlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);
        mContext = getApplicationContext();

        prepareMenu();

        TextView title = findViewById(R.id.title);
        title.setText("찜 목록");
        ImageButton backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(this);
        

        mRecyclerView = findViewById(R.id.mywish_list);
        mWishlistAdapter = new WishlistAdapter(mContext, mWishList);
        mRecyclerView.setAdapter(mWishlistAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void prepareMenu(){
        //TODO: 찜한 여행지 읽어와서 ArrayList에 담기
        mWishList = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            MarkerInfo data = new MarkerInfo(this);
            
            data.setName("hello " + i);
            data.setAddress("서울특별시");
            data.setImage(R.drawable.daegu);
            data.setSummary("어디어디어디임");
            data.setRate(4);
            data.setLike(true);
    
            mWishList.add(data);
        }
        
        for (int i = 0; i < 3; i++) {
            Log.d(TAG, "mWishList name: " + mWishList.get(i).getName());
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                finish();
                break;
        }
    }
}
