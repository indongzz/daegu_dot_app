package com.kop.daegudot.MorePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kop.daegudot.R;

import java.util.ArrayList;

public class MyReviewAndCommentAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mArrayList;
    private ViewHolder mViewHolder;

    public MyReviewAndCommentAdapter(Context context, ArrayList<String> arrayList){
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
        return convertView;
    }

    public class ViewHolder{
        private TextView mTextView;
        public ViewHolder(View view){
            mTextView = (TextView) view.findViewById(R.id.text1);
        }
    }
}
