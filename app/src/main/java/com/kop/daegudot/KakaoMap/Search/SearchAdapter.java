package com.kop.daegudot.KakaoMap.Search;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.Network.Map.Place;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.Collection;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {
    private static final String TAG = "SearchAdapter";
    
    private Context mContext;
    private ArrayList<Place> mSearchlist;
    private ArrayList<Place> mPlaceList;
    
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView searchName;
        TextView searchAddress;
        
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            
            searchName = itemView.findViewById(R.id.search_name);
            searchAddress = itemView.findViewById(R.id.search_address);
            
            itemView.setOnClickListener(this);
        }
    
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.search_list) {
                Toast.makeText(mContext, mPlaceList.get(getAdapterPosition()).attractName, Toast.LENGTH_SHORT).show();
                
                ((MapMainActivity) mContext).moveToPlace(mPlaceList.get(getAdapterPosition()));
            }
        }
    }
    
    public SearchAdapter(Context context, ArrayList<Place> placeList) {
        mContext = context;
        mPlaceList = placeList;
        mSearchlist = mPlaceList;
    }
    
    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_search_list, parent, false);
    
        return new SearchViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
            Place item = mPlaceList.get(position);
            
            holder.searchName.setText(item.attractName);
            holder.searchAddress.setText(item.address);
    }
    
    @Override
    public int getItemCount() {
        return mSearchlist.size();
    }
    
    @Override
    public Filter getFilter() {
        return searchFilter;
    }
    
    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Place> filteredList = new ArrayList<>();
            
            Handler handler = new Handler();
            handler.post(() -> {
                if (constraint != null || constraint.toString().length() != 0) {
                    String filterPattern = constraint.toString().toLowerCase().trim();
    
                    for (Place item : mSearchlist) {
                        if (item.attractName.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
            });
            
            FilterResults results = new FilterResults();
            results.values = filteredList;
            
            Log.d(TAG, "search filter done");
            return results;
        }
    
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mSearchlist.clear();
            mSearchlist.addAll((ArrayList) results.values);
            
            notifyDataSetChanged();
        }
    };
}
