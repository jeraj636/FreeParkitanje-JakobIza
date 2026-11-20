package com.example.freeparkiranje.prijavniSistem;

import android.content.Context;

import com.example.freeparkiranje.api.ApiClient;
import com.example.freeparkiranje.model.prijavaAndroid.PrijavaAndroidRequest;
import com.example.freeparkiranje.model.prijavaAndroid.PrijavaAndroidResponse;
import com.example.freeparkiranje.model.prijavaToken.PrijavaTokenRequest;
import com.example.freeparkiranje.model.prijavaToken.PrijavaTokenResponse;
import com.example.freeparkiranje.token.Token;

import java.io.IOException;

public class PrijavniSistem {
    public  static  boolean prijavaSPodatki(Context con, String upIme, String upGeslo) throws IOException {
        PrijavaAndroidResponse odgovor = (PrijavaAndroidResponse) ApiClient.getApi().prijavaAndroid(new PrijavaAndroidRequest(upIme,upGeslo)).execute().body();

        if(odgovor.aliJeOk()){
            Token.setToken(con,odgovor.getToken());
        }
        return odgovor.aliJeOk();
    }
    public static boolean prijavaSToken(String token) throws IOException{
        if(token == null)
            return false;
       PrijavaTokenResponse odgovor = (PrijavaTokenResponse) ApiClient.getApi().prijavaToken(new PrijavaTokenRequest(token)).execute().body();
       return odgovor.aliJeOk();
    }
}
