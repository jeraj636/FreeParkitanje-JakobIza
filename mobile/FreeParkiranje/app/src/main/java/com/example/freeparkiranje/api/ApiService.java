package com.example.freeparkiranje.api;

import com.example.freeparkiranje.model.avtoDodaj.AvtoDodajRequest;
import com.example.freeparkiranje.model.avtoDodaj.AvtoDodajResponse;
import com.example.freeparkiranje.model.avtoIzbris.AvtoIzbrisRequest;
import com.example.freeparkiranje.model.avtoIzbris.AvtoIzbrisResponse;
import com.example.freeparkiranje.model.cudezPoisci.CudezPoisciResponse;
import com.example.freeparkiranje.model.jaz.JazRequest;
import com.example.freeparkiranje.model.jaz.JazResponse;
import com.example.freeparkiranje.model.prijavaAndroid.PrijavaAndroidRequest;
import com.example.freeparkiranje.model.prijavaAndroid.PrijavaAndroidResponse;
import com.example.freeparkiranje.model.prijavaToken.PrijavaTokenRequest;
import com.example.freeparkiranje.model.prijavaToken.PrijavaTokenResponse;
import com.example.freeparkiranje.model.prostost.ProstostRequest;
import com.example.freeparkiranje.model.prostost.ProstostResponse;
import com.example.freeparkiranje.model.rezervacijaDodaj.RezervacijaDodajRequest;
import com.example.freeparkiranje.model.rezervacijaDodaj.RezervacijaDodajResponse;
import com.example.freeparkiranje.model.rezervacijaPrihodnje.RezervacijaPrihodnjeRequest;
import com.example.freeparkiranje.model.rezervacijaPrihodnje.RezervacijaPrihodnjeResponse;
import com.example.freeparkiranje.model.rezervacijaSkenslaj.RezervacijaSkenslajResponse;
import com.example.freeparkiranje.model.rezervacijaSkenslaj.RezervacijeSkenslajRequest;
import com.example.freeparkiranje.model.cudezPoisci.CudezPoisciRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("prijavaAndroid")
    Call<PrijavaAndroidResponse> prijavaAndroid(@Body PrijavaAndroidRequest zahteva);

    @POST("prijavaToken")
    Call<PrijavaTokenResponse> prijavaToken(@Body PrijavaTokenRequest zahteva);
    @POST("jaz")
    Call<JazResponse> jaz(@Body JazRequest zahteva);

    @POST("avto/skenslaj")
    Call<AvtoIzbrisResponse> avtoSkenslaj(@Body AvtoIzbrisRequest zahteva);

    @POST("avto/dodaj")
    Call<AvtoDodajResponse> avtoDodaj(@Body AvtoDodajRequest zahteva);

    @POST("prostost")
    Call<ProstostResponse> prostost(@Body ProstostRequest zahteva);

    @POST("rezervacija/dodaj")
    Call<RezervacijaDodajResponse> rezervacijaDodaj(@Body RezervacijaDodajRequest zahteva);

    @POST("rezervacija/prihodnje")
    Call<RezervacijaPrihodnjeResponse> rezervacijaPrihodnje(@Body RezervacijaPrihodnjeRequest zahteva);

    @POST("rezervacija/skenslaj")
    Call<RezervacijaSkenslajResponse> rezervacijaSkenslaj(@Body RezervacijeSkenslajRequest zahteva);

    @POST("cudez/poisciRezervacijo")
    Call<CudezPoisciResponse> cudezPoisci(@Body CudezPoisciRequest zahteva);

}
