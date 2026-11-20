package com.example.freeparkiranje.model;

public class PreprostResponse  implements Response{
    private String info;
    public boolean aliJeOk(){
        return info.equalsIgnoreCase("ok");
    }

}
