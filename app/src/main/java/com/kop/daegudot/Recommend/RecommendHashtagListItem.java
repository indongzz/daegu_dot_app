package com.kop.daegudot.Recommend;

import android.graphics.drawable.Drawable;

public class RecommendHashtagListItem {
    private Drawable image;
    private String hashtag;
    
    public Drawable getImage() {
        return image;
    }
    
    public void setImage(Drawable image) {
        this.image = image;
    }
    
    public String getHashtag() {
        return hashtag;
    }
    
    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}
