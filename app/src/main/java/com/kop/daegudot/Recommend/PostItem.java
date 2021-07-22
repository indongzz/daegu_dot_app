package com.kop.daegudot.Recommend;

import android.os.Parcel;
import android.os.Parcelable;

import com.kop.daegudot.MySchedule.MainScheduleInfo;

public class PostItem implements Parcelable {
    private String title;
    private String content;
    private float rating;
    private String writer;
    private int commentNum;
    private int id;
    
    public PostItem() {
    
    }
    
    public PostItem(Parcel in) {
        title = in.readString();
        content = in.readString();
        rating = in.readFloat();
        writer = in.readString();
        commentNum = in.readInt();
        id = in.readInt();
    }
    
    public static final Creator<PostItem> CREATOR = new Creator<PostItem>() {
        @Override
        public PostItem createFromParcel(Parcel in) {
            return new PostItem(in);
        }
        
        @Override
        public PostItem[] newArray(int size) {
            return new PostItem[size];
        }
    };
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public float getRating() {
        return rating;
    }
    
    public void setRating(float rating) {
        this.rating = rating;
    }
    
    public String getWriter() {
        return writer;
    }
    
    public void setWriter(String writer) {
        this.writer = writer;
    }
    
    public int getCommentNum() {
        return commentNum;
    }
    
    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCommentString() {
        return "댓글" + getCommentNum() + "개";
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeFloat(rating);
        dest.writeString(writer);
        dest.writeInt(commentNum);
        dest.writeInt(id);
    }
}
