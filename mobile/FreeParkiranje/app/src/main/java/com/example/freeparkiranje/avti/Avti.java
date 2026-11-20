package com.example.freeparkiranje.avti;

import com.example.freeparkiranje.api.ApiClient;
import com.example.freeparkiranje.model.avtoDodaj.AvtoDodajRequest;
import com.example.freeparkiranje.model.avtoDodaj.AvtoDodajResponse;
import com.example.freeparkiranje.model.avtoIzbris.AvtoIzbrisRequest;
import com.example.freeparkiranje.model.avtoIzbris.AvtoIzbrisResponse;
import com.example.freeparkiranje.model.jaz.JazRequest;
import com.example.freeparkiranje.model.jaz.JazResponse;

import java.io.IOException;

public class Avti {
    public static String[] DobiMojeAvte() throws IOException{
        JazResponse odgovor = ApiClient.getApi().jaz(new JazRequest()).execute().body();
        if(odgovor == null)
            throw new IOException();
        if(!odgovor.aliJeOk()){
            throw new IOException("neki");
        }
        return odgovor.getAvti();
    }
    public  static  void IzbrisiAvto(String registrska) throws IOException{
        AvtoIzbrisResponse odgovor = ApiClient.getApi().avtoSkenslaj(new AvtoIzbrisRequest(registrska)).execute().body();
        if(odgovor== null || !odgovor.aliJeOk()){
            throw new IOException("neki");
        }

    }
    public static void DodajAvto(String registrska) throws IOException{
        AvtoDodajResponse odgovor = ApiClient.getApi().avtoDodaj(new AvtoDodajRequest(registrska)).execute().body();
        if(odgovor== null || !odgovor.aliJeOk()){
            throw new IOException("neki");
        }
    }
}
