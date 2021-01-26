package com.kop.daegudot.Recommend;

public class PostList {
    private String title;
    private String content;
    private float rating;
    private String writer;
    private int commentNum;
    private int id;
    
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
}
