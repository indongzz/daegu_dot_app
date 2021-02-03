package com.kop.daegudot.MorePage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class MoreAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mArrayList;
    private ArrayList<Integer> mIconList;
    private ViewHolder mViewHolder;

    public MoreAdapter(Context context, ArrayList<String> arrayList, ArrayList<Integer> iconList){
        this.mContext = context;
        this.mArrayList = arrayList;
        mIconList = iconList;
    }
    public MoreAdapter(Context context, ArrayList<String> arrayList){
        this.mContext = context;
        this.mArrayList = arrayList;
    }
    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_more_list,parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }
        else mViewHolder = (ViewHolder) convertView.getTag();
        mViewHolder.mTextView.setText(mArrayList.get(position));
        if (mIconList != null) {
            mViewHolder.mImageView.setImageResource(mIconList.get(position));
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(70, 70);
            ll.gravity = Gravity.CENTER_VERTICAL;
            ll.setMargins(0, 0, 20, 0);
            mViewHolder.mImageView.setLayoutParams(ll);
        }
        return convertView;
    }

    public class ViewHolder{
        private TextView mTextView;
        private ImageView mImageView;
        public ViewHolder(View view){
            mTextView = (TextView) view.findViewById(R.id.text1);
            mImageView = view.findViewById(R.id.icon);
        }
    }

}
