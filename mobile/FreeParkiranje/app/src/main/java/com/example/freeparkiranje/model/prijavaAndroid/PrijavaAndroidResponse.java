package com.example.freeparkiranje.model.prijavaAndroid;

import com.example.freeparkiranje.model.Response;

public class PrijavaAndroidResponse implements Response {

        private String info;
        private String token;

        public boolean aliJeOk(){
            return info.equalsIgnoreCase("ok");
        }
        public String getToken() {
            return token;
        }
}
