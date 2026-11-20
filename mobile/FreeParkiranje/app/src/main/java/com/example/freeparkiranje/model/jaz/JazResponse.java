package com.example.freeparkiranje.model.jaz;


import com.example.freeparkiranje.model.Response;

public class JazResponse implements Response {

    private String info;
    private String role;
    private String ime;
    private String[] avti;

    public boolean aliJeOk(){
        return info.equalsIgnoreCase("ok");
    }
    public String getIme() {
        return ime;
    }
    public String getRole() {
        return role;
    }
    public String getInfo(){return info;}
    public String[] getAvti() {
        return avti;
    }

}