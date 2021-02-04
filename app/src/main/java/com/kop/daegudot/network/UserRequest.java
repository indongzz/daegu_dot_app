package com.kop.daegudot.network;

public class UserRequest {
    public String email;
    public String nickname;
    public String password;
    public char type;

    public UserRequest() {

    }

    public UserRequest(String email, String nickname, String password, char type) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.type = type;
    }
}
