package com.kop.daegudot.MorePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.kop.daegudot.MainActivity;
import com.kop.daegudot.MorePage.MyInformation.MyInformationActivity;
import com.kop.daegudot.MorePage.MyReview.MyCommentActivity;
import com.kop.daegudot.MorePage.MyReview.MyReviewStoryActivity;
import com.kop.daegudot.R;

import java.util.ArrayList;

public class MoreFragment extends Fragment implements View.OnClickListener{
    View view;
    private Context mContext;
    private ArrayList<String> mArrayList;
    private ArrayList<Integer> mIconList;
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

        TextView title = view.findViewById(R.id.title);
        title.setText("더보기");
        
        ImageButton backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);

        mListView = view.findViewById(R.id.more_list);

        mMoreAdpater = new MoreAdapter(mContext, mArrayList, mIconList);
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
        
        mIconList = new ArrayList<>();
        mIconList.add(R.drawable.profile_image);
        mIconList.add(R.drawable.mypost);
        mIconList.add(R.drawable.mycomment);
        mIconList.add(R.drawable.wish);
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
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            // Move to Recommend Fragment
            ((MainActivity)mContext).changeFragment(3, 2);
        }
    }
}