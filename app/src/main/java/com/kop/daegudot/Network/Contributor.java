package com.kop.daegudot.Network;

import androidx.annotation.NonNull;

public class Contributor {
    String login;
    String url;
    int id;

    @NonNull
    @Override
    public String toString() {
        return "login : " + login + " id : " + id + " url : " + url;
    }
}
