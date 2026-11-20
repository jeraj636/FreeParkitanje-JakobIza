package com.example.freeparkiranje.parking;

public class Polozaj{
    public   int x;
    public int y;
    public   int smer;
    public int i;
    public boolean zaseden=false;
    public Polozaj(int x,int y,int smer){
        this.x = x;
        this.y = y;
        this.smer = smer;
    }

}