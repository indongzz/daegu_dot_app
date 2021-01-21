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

public class MyCommentActivity extends AppCompatActivity {
    private Context mContext;
    private ArrayList<String> mArrayList;
    private ListView mListView;
    private MyReviewAndCommentAdapter mMyReviewAndCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        this.mContext = getApplicationContext();

        prepareMenu();

        // TODO: 뒤로가기 버튼 구현 필요
        TextView title = findViewById(R.id.title);
        title.setText("내가 쓴 댓글");

        mListView = findViewById(R.id.mycomment_list);
        mListView.setOnItemClickListener(onItemClickListener);
        mMyReviewAndCommentAdapter = new MyReviewAndCommentAdapter(mContext, mArrayList);
        mListView.setAdapter(mMyReviewAndCommentAdapter);
    }

    private void prepareMenu(){
        mArrayList = new ArrayList<>();
        mArrayList.add("내가 쓴 댓글 1");
        mArrayList.add("내가 쓴 댓글 2");
        mArrayList.add("내가 쓴 댓글 3");
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switch (position){
                case 0:
                    Toast.makeText(mContext, "내가 쓴 댓글 1",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(mContext, "내가 쓴 댓글 2",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(mContext, "내가 쓴 댓글 3",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
