package com.kop.daegudot.Recommend;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.view.GravityCompat;

import com.kop.daegudot.R;

public class DrawerHandler {
    PostList mPost;
    View mView;
    
    TextView postTitle, postWriter, postContent;
    RatingBar postRating;
    
    DrawerHandler(View view, PostList post) {
        mPost = post;
        mView = view;
        
        bindView();
    }
    
    public void bindView() {
        postTitle = mView.findViewById(R.id.post_title);
        postWriter = mView.findViewById(R.id.post_writer);
        postRating = mView.findViewById(R.id.post_rating);
        postContent = mView.findViewById(R.id.post_content);
    }
    
    public void setDrawer() {
        postTitle.setText(mPost.getTitle());
        postRating.setRating(mPost.getRating());
        postWriter.setText(mPost.getWriter());
        postContent.setText(mPost.getContent());

    }
}
