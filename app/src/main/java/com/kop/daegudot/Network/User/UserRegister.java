package com.kop.daegudot.Network.User;

public class UserRegister {
    private String email;
    private String nickname;
    private String password;
    private char type;

    public UserRegister(String email, String password, String nickname, char type){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.type = type;
    }
}
