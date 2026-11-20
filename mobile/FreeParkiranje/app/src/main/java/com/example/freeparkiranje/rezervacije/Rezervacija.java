package com.example.freeparkiranje.rezervacije;

public class Rezervacija {
    public int id;
    public String zac;
    public String kon;
    public  int parkingID;
    public String registrska;
    public String toString(){
        return "Avto: "+this.registrska + "\nod: " + zac + "\ndo: " + kon;
    }
}
