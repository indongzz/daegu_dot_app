package com.kop.daegudot.MorePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class MoreFragment<onItemClickListener> extends Fragment{
    View view;
    private Context mContext;
    private ArrayList<String> mArrayList;
    private ListView mListView;
    private MoreAdapter mMoreAdpater;

    public MoreFragment() {
        prepareMenu();
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_more, container, false);

        // TODO: 뒤로가기 버튼 구현 필요
        TextView title = view.findViewById(R.id.title);
        title.setText("더보기");

        mListView = (ListView) view.findViewById(R.id.more_list);

        mMoreAdpater = new MoreAdapter(mContext, mArrayList);
        mListView.setAdapter(mMoreAdpater);
        mListView.setOnItemClickListener(onItemClickListener);

        // Inflate the layout for this fragment
        return view;
    }

    private void prepareMenu(){
        mArrayList = new ArrayList<>();
        mArrayList.add("내 정보 확인");
        mArrayList.add("내가 쓴 글");
        mArrayList.add("내가 쓴 댓글");
        mArrayList.add("찜");
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Intent intent;
            switch (position){
                case 0: //내 정보 확인
                    intent = new Intent(mContext, MyInformationActivity.class);
                    startActivity(intent);
                    break;
                case 1: //내가 쓴 글
                    intent = new Intent(mContext, MyReviewStoryActivity.class);
                    startActivity(intent);
                    break;
                case 2: //내가 쓴 댓글
                    intent = new Intent(mContext, MyCommentActivity.class);
                    startActivity(intent);
                    break;
                case 3: //찜
                    intent = new Intent(mContext, MyWishlistActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

}