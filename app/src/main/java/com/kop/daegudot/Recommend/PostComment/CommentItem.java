package com.kop.daegudot.Recommend.PostComment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentItem {
    private String writer;
    private String content;
    private LocalDateTime time;
    
    public String getWriter() {
        return writer;
    }
    
    public void setWriter(String writer) {
        this.writer = writer;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getTime() {
        return time;
    }
    
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    
    public String getStringTime() {
        return time.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
    }
}
