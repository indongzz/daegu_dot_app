package com.kop.daegudot.Recommend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kop.daegudot.MainActivity;
import com.kop.daegudot.Network.Recommend.Hashtag.HashtagResponse;
import com.kop.daegudot.Network.Recommend.Hashtag.HashtagResponseList;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.R;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 추천 탭 Fragment
 * 추천글 작성 및 조회 가능
 */
public class RecommendFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RecommendFragment";
    View mView;
    
    ChipGroup mChipGroup;
    
    ListView mListView;
    RecommendHashtagListAdapter adapter;
    
    public ProgressBar progressBar;     // 로딩 중
    
    /* Rx java */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    static ArrayList<HashtagResponse> mHashtags;
    private String mToken;
    
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
    
        SharedPreferences pref = this.getActivity()
                .getSharedPreferences("data", Context.MODE_PRIVATE);
        mToken = pref.getString("token", "");
        
        TextView title = mView.findViewById(R.id.title);
        title.setText("추천");
        
        ImageButton backBtn = mView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
    
        progressBar = mView.findViewById(R.id.progress_bar);
    
        // hashtag buttons setting
        mChipGroup = mView.findViewById(R.id.btns_group);
        mHashtags = new ArrayList<>();
        selectHashtagsRx();
        
        mListView = mView.findViewById(R.id.listview);
        adapter = new RecommendHashtagListAdapter();
        
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), RecommendListActivity.class);
            intent.putExtra("hashtag", mHashtags.get(position));
            startActivity(intent);
        });
        
        // 우측 하단 글 작성 버튼
        FloatingActionButton fab = mView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        
        return mView;
    }
    
    public void setHashtagBtns() {
        int n = mHashtags.size();
        
        final Chip[] chips = new Chip[n];
    
        for(int i = 0; i < n; i++) {
            chips[i] = (Chip)getLayoutInflater()
                    .inflate(R.layout.layout_chip_hash1, mChipGroup, false);
            chips[i].setText(mHashtags.get(i).content);
            chips[i].setTag(mHashtags.get(i).content);
            chips[i].setId(i);
            chips[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mListView.smoothScrollToPositionFromTop(v.getId(), 0, 100);
                }
            });
            mChipGroup.addView(chips[i]);
        }
    }
    
    public void setRecommendImageList() {
        // TODO: change pictures for each hashtags
        
        int n = mHashtags.size();
        Drawable[] drawables = new Drawable[n];
        drawables[0] = ContextCompat.getDrawable(getActivity(), R.drawable.picex);
        drawables[1] = ContextCompat.getDrawable(getActivity(), R.drawable.pic_eat);
        
        for (int i = 0; i < n; i++) {
            adapter.addItem(drawables[i%2],"#" + mHashtags.get(i).content);
        }
    
        mListView.setAdapter(adapter);
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            Intent intent = new Intent(getContext(), AddRecommendActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.backBtn) {
            ((MainActivity) getActivity()).changeFragment(2, 1);
        }
    }
    
    public void selectHashtagsRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi(mToken);
        Observable<HashtagResponseList> observable = service.selectHashtagList();
        
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<HashtagResponseList>() {
                    @Override
                    public void onNext(HashtagResponseList response) {
                        Log.d("RX " + TAG, "Next");
                        
                        mHashtags.addAll(response.hashtagResponseDtoArrayList);
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");
    
                        setHashtagBtns();
                        setRecommendImageList();
                        progressBar.setVisibility(View.GONE);
                    }
                })
        );
    }
    
    public static ArrayList<HashtagResponse> getHashtags() {
        return mHashtags;
    }
}