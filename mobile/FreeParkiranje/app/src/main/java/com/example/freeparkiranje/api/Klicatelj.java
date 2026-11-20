package com.example.freeparkiranje.api;

import com.example.freeparkiranje.model.prijavaAndroid.PrijavaAndroidRequest;
import com.example.freeparkiranje.model.prijavaAndroid.PrijavaAndroidResponse;

import java.io.IOException;

public class Klicatelj {
    public  static PrijavaAndroidResponse prijavaAndroid(PrijavaAndroidRequest zahteva)throws IOException {
        return ApiClient.getApi()
                .prijavaAndroid(zahteva)
                .execute()
                .body();
    }
}
