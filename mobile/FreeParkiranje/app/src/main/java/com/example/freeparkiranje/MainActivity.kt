package com.example.freeparkiranje

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.freeparkiranje.prijavniSistem.PrijavniSistem
import com.example.freeparkiranje.token.Token
import com.example.freeparkiranje.ui.components.Avti.VsebinaAvti
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.Ogrodje
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.Spodaj
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.Zgoraj
import com.example.freeparkiranje.ui.components.PrijavnoOkno
import com.example.freeparkiranje.ui.components.RPM.VsebinaRPM
import com.example.freeparkiranje.ui.components.Rezervacije.VsebinaRezervacije
import com.example.freeparkiranje.ui.components.ZacetnoNalaganje
import com.example.freeparkiranje.ui.components.pogovor.VsebinaPogovor
import com.example.freeparkiranje.ui.theme.FreeParkiranjeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FreeParkiranjeTheme {
                var seNalaga by remember { mutableStateOf(true) }
                var prijavljen by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    val token = withContext(Dispatchers.IO) {
                        Token.getToken(this@MainActivity)
                    }
                    if (token.isNotEmpty()) {
                        val aliJeTokenVeljaven = withContext(Dispatchers.IO) {
                            PrijavniSistem.prijavaSToken(token)
                        }
                        prijavljen = aliJeTokenVeljaven
                    } else {
                        prijavljen = false
                    }
                    seNalaga = false
                }


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (seNalaga) {
                        ZacetnoNalaganje()
                    } else {
                        if (prijavljen) {
                            var zaslon by remember { mutableStateOf("RPM") }

                            Ogrodje(zgoraj = { Zgoraj(
                                naslov = when (zaslon) {
                                    "RPM" -> "Parkirišče"
                                    "Avto" -> "Urejanje avtomobilov"
                                    "Rezervacija" -> "Moje rezervacije"
                                    else -> "Parkirišče"
                                }
                            ) }, spodaj = {
                                Spodaj(
                                    naKlikRPM = { zaslon = "RPM" },
                                    naKlikAvti = {zaslon="Avto"},
                                    naKlikRezervaijca = {zaslon= "Rezervacija"},
                                    naKlikPogovor = {zaslon="Pog"},
                                    aktivnez = zaslon
                                )
                            }) { p ->
                                Column(Modifier
                                    .fillMaxSize()
                                    .padding(p)) {
                                    when (zaslon) {
                                        "RPM" -> {
                                            VsebinaRPM()
                                        }
                                        "Avto"->{
                                            VsebinaAvti()
                                        }
                                        "Rezervacija"->{
                                            VsebinaRezervacije()
                                        }
                                        "Pog" -> {
                                            VsebinaPogovor()
                                        }
                                    }
                                }
                            }
                        } else {
                            PrijavnoOkno(this, obUspesniPrijavi = {
                                prijavljen = true

                            })
                        }
                    }
                }

            }
        }
    }
}







