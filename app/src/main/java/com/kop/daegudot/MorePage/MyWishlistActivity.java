package com.kop.daegudot.MorePage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class MyWishlistActivity extends AppCompatActivity {
    private Context mContext;
    private ArrayList<String> mArrayList;
    private ListView mListView;
    private WishlistAdapter mWishlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);
        mContext = getApplicationContext();

        prepareMenu();

        // TODO: 뒤로가기 버튼 구현 필요
        TextView title = findViewById(R.id.title);
        title.setText("찜 목록");

        /*TODO: 찜 리스트 xml 수정 필요
           -> 현재는 WishlistAdapter에서 더보기 첫 페이지에서 사용되는 xml 사용*/
        mListView = (ListView) findViewById(R.id.mywish_list);
        mListView.setOnItemClickListener(onItemClickListener);
        mWishlistAdapter = new WishlistAdapter(mContext, mArrayList);
        mListView.setAdapter(mWishlistAdapter);
    }

    //TODO: 찜한 여행지 읽어와서 ArrayList에 담기
    private void prepareMenu(){
        mArrayList = new ArrayList<>();
        mArrayList.add("여기에");
        mArrayList.add("찜한 여행지");
        mArrayList.add("넣어주세요!");
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switch (position){
                case 0:
                    Toast.makeText(mContext, "여기에",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(mContext, "찜한 여행지",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(mContext, "넣어주세요!",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
