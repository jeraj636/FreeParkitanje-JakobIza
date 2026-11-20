package com.example.freeparkiranje.model.rezervacijaDodaj;

public class RezervacijaDodajRequest {
    private String zac;
    private String kon;
    private int parkingID;
    private String registrska;

    public  RezervacijaDodajRequest(String zac,String kon,int parking,String registrska){
        this.kon = kon;
        this.zac = zac;
        this.parkingID = parking;
        this.registrska = registrska;
    }
}
