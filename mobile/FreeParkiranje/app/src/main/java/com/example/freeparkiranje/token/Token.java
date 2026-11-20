package com.example.freeparkiranje.token;

import android.content.Context;
import android.content.SharedPreferences;

public class Token {
    private static final String PREFS = "freeparkiranje";
    private static final String KEY = "auth_token";
    public static String getToken(Context con){
        SharedPreferences sp = con.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        return sp.getString(KEY,"");
    }
    public  static  void  setToken(Context con, String token){
        SharedPreferences sp = con.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        sp.edit().putString(KEY,token).apply();
    }
}
