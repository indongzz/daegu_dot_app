package com.kop.daegudot.Login;

public class User {
    String email;
    String nickName;
    String password;
    char type;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public char getType() {
        return type;
    }
    
    public void setType(char type) {
        this.type = type;
    }
}
