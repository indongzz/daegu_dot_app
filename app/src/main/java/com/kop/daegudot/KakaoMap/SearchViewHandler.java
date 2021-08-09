package com.kop.daegudot.KakaoMap;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.widget.SearchView;

public class SearchViewHandler {
    private static final String TAG = "SearchViewHandler";
    Context mContext;
    
    public SearchViewHandler(Context context) {
        mContext = context;
    }
    
    public void setSearchView() {
        ((MapMainActivity) mContext).mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "submitted: " + query);
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "changed: " + newText);
                return false;
            }
        });
    }
}
