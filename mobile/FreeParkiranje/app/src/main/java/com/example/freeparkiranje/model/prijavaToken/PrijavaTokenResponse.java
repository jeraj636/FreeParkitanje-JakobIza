package com.example.freeparkiranje.model.prijavaToken;

import com.example.freeparkiranje.model.Response;

public class PrijavaTokenResponse implements Response {
    private String info;

    public boolean aliJeOk(){
        return info.equalsIgnoreCase("ok");
    }
}
