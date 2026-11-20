package com.example.freeparkiranje.cudez;

import com.example.freeparkiranje.api.ApiClient;
import com.example.freeparkiranje.model.cudezPoisci.CudezPoisciRequest;
import com.example.freeparkiranje.model.cudezPoisci.CudezPoisciResponse;

import java.io.IOException;

public class Cudez {
    public static String zac="";
    public  static String kon="";
    public static void poisciRezervacijo(String besdilo) throws IOException{
        CudezPoisciResponse odgovor = ApiClient.getApi().cudezPoisci(new CudezPoisciRequest(besdilo)).execute().body();
        if(odgovor == null || !odgovor.aliJeOk())
        {
            throw new IOException();
        }
        zac = odgovor.zac;
        kon = odgovor.kon;
    }
}
