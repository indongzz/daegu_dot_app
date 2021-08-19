package com.kop.daegudot.KakaoMap.Search;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.KakaoMap.Search.SearchAdapter;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.Map;

public class SearchViewHandler {
    private static final String TAG = "SearchViewHandler";
    Context mContext;
    private SearchAdapter mAdapter;
    private RecyclerView mRecyclerView;
    
    public SearchViewHandler(Context context) {
        mContext = context;
    }
    
    public void setViews() {
        mRecyclerView = ((MapMainActivity) mContext).findViewById(R.id.recyclerview_search);
        
        mAdapter = new SearchAdapter(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, 1));
        mRecyclerView.bringToFront();
    }
    
    public void setSearchView() {
        setViews();
        mRecyclerView.setVisibility(View.VISIBLE);
        
        ((MapMainActivity) mContext).mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "submitted: " + query);
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "changed: " + newText);
                mAdapter.getFilter().filter(newText);
                
                return false;
            }
        });
    }
    
    public void closeRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }
}
