package com.example.freeparkiranje.rezervacije;

import android.util.Log;

import com.example.freeparkiranje.api.ApiClient;
import com.example.freeparkiranje.model.rezervacijaDodaj.RezervacijaDodajRequest;
import com.example.freeparkiranje.model.rezervacijaDodaj.RezervacijaDodajResponse;
import com.example.freeparkiranje.model.rezervacijaPrihodnje.RezervacijaPrihodnjeRequest;
import com.example.freeparkiranje.model.rezervacijaPrihodnje.RezervacijaPrihodnjeResponse;
import com.example.freeparkiranje.model.rezervacijaSkenslaj.RezervacijaSkenslajResponse;
import com.example.freeparkiranje.model.rezervacijaSkenslaj.RezervacijeSkenslajRequest;

import java.io.IOException;

public class Rezervacije {
    public static void Rezerviraj(String zac,String kon,int parking,String reg) throws IOException {
        Log.d("R", zac  );
        Log.d("R", kon    );
        Log.d("R", "" + parking  );
        Log.d("R", reg  );


        RezervacijaDodajResponse odgovor = ApiClient.getApi().rezervacijaDodaj(new RezervacijaDodajRequest(zac,kon,parking,reg)).execute().body();

    }
    public static Rezervacija[] prihodnjeRezervacije() throws IOException{
        RezervacijaPrihodnjeResponse odgovor = ApiClient.getApi().rezervacijaPrihodnje(new RezervacijaPrihodnjeRequest()).execute().body();
        if(odgovor == null || !odgovor.aliJeOk())
        {
            throw new IOException();
        }
        return odgovor.rezervacije;

    }
    public static void skenslaj(int id) throws IOException {
        RezervacijaSkenslajResponse odgovor = ApiClient.getApi().rezervacijaSkenslaj(new RezervacijeSkenslajRequest(id)).execute().body();

    }
}
