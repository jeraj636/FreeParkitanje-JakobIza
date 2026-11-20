package com.example.freeparkiranje.parking;

import android.util.Log;

import com.example.freeparkiranje.api.ApiClient;
import com.example.freeparkiranje.model.prostost.ProstostRequest;
import com.example.freeparkiranje.model.prostost.ProstostResponse;

import java.io.IOException;


public class Parking {
    public static Polozaj[] dobiParkinge() {
        Polozaj[] p = new Polozaj[86];
        int index = 0;

        for (int i = 0; i < 17; i++)
            p[index++] = new Polozaj(3 + i, 0, 1);
        for (int i = 0; i < 15; i++)
            p[index++] = new Polozaj(3 + i, 5, 1);
        for (int i = 0; i < 15; i++)
            p[index++] = new Polozaj(3 + i, 7, 1);
        for (int i = 0; i < 13; i++)
            p[index++] = new Polozaj(8 + i, 12, 1);
        for (int i = 0; i < 6; i++)
            p[index++] = new Polozaj(5 + 2 * i, 3, 0);
        for (int i = 0; i < 6; i++)
            p[index++] = new Polozaj(5 + 2 * i, 10, 0);
        for (int i = 0; i < 4; i++)
            p[index++] = new Polozaj(0, 2 + i, 0);
        for (int i = 0; i < 10; i++)
            p[index++] = new Polozaj(21, 2 + i, 0);

        for (int i = 0; i < p.length; i++)
            p[i].i = i + 1;

        return p;

    }

    public static Polozaj[] dobiZasedenaParkinge(String zac, String kon) throws IOException {

        Polozaj[] p = dobiParkinge();

        ProstostResponse odgovor = ApiClient.getApi().prostost(new ProstostRequest(zac, kon)).execute().body();

        if (odgovor == null || !odgovor.aliJeOk()) {
            throw new IOException();
        }
        for(Polozaj pol:p){
            pol.zaseden = true;
        }
        for (String z : odgovor.getProstiParkirplaci()) {
            Log.d("TUKAJ", z);
            int i = Integer.parseInt(z)-1;
            p[i].zaseden = false;
        }
        return p;
    }

}
