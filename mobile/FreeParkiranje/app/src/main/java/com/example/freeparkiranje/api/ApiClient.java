package com.example.freeparkiranje.api;

import java.net.CookiePolicy;
import java.net.CookieManager;

import okhttp3.OkHttpClient;
import okhttp3.JavaNetCookieJar;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String API_URL =
            "https://fp.kokosek.si/api/";

    private static final CookieManager cookieManager =
            new CookieManager(null, CookiePolicy.ACCEPT_ALL);

    private static final OkHttpClient client =
            new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();

    private static final Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    public static ApiService getApi() {
        return retrofit.create(ApiService.class);
    }
}
