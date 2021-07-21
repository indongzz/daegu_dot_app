package com.kop.daegudot.Recommend;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class RecommendHashtagListAdapter extends BaseAdapter {
    private ArrayList<RecommendHashtagListItem> listViewItemList = new ArrayList<>();
    
    @Override
    public int getCount() {
        return listViewItemList.size();
    }
    
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate
                    (R.layout.layout_recommend_hashtag_listitem, parent, false);
        }
    
        ImageView imageView = convertView.findViewById(R.id.image);
        TextView hashtag = convertView.findViewById(R.id.image_text);
        
        RecommendHashtagListItem listViewItem = listViewItemList.get(position);
        
        imageView.setImageDrawable(listViewItem.getImage());
        hashtag.setText(listViewItem.getHashtag());
        
        return convertView;
    }
    
    public void addItem(Drawable image, String hashtag) {
        RecommendHashtagListItem item = new RecommendHashtagListItem();
        
        item.setImage(image);
        item.setHashtag(hashtag);
        
        listViewItemList.add(item);
    }
}
