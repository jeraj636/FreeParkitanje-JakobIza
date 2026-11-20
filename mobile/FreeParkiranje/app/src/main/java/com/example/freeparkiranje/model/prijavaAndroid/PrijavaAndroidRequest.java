package com.example.freeparkiranje.model.prijavaAndroid;

public class PrijavaAndroidRequest {
    private String upIme;
    private String upGeslo;

   public PrijavaAndroidRequest(String upIme, String upGeslo){
        this.upIme = upIme;
        this.upGeslo = upGeslo;
    }
    public String getUpIme() {
        return upIme;
    }

    public String getUpGeslo() {
        return upGeslo;
    }
}
