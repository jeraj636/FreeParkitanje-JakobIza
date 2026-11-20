package com.example.freeparkiranje.ui.components.Rezervacije

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.freeparkiranje.R
import com.example.freeparkiranje.rezervacije.Rezervacija
import com.example.freeparkiranje.rezervacije.Rezervacije
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.kartica.Kartica
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.vsebina.Vsebina
import com.example.freeparkiranje.ui.components.ZacetnoNalaganje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun IzbrisiRezervacijo(rezervacijaID: Int, napis: String, naKlik: (() -> Unit)) {
    val coroutineScope = rememberCoroutineScope() // Potrebno za izvajanje IO operacij ob kliku

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(2f)) {
            Text(napis, color = MaterialTheme.colorScheme.secondary)
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,

        ) {
            Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()){
                Card(
                    Modifier.clickable(onClick = { coroutineScope.launch(Dispatchers.IO) { naKlik() } }),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.kos),
                        contentDescription = stringResource(R.string.kos),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
    HorizontalDivider()

}

@Composable
fun VsebinaRezervacije() {
    var mojeRezervacije by remember { mutableStateOf(emptyList<Rezervacija>()) }
    var nalagam by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope() // Potrebno za izvajanje IO operacij ob kliku

    LaunchedEffect(Unit) {
        mojeRezervacije = withContext(Dispatchers.IO) {
            Rezervacije.prihodnjeRezervacije().toList()
        }
        nalagam = false
    }
    if (!nalagam) {
        Vsebina {
            Kartica(naslov = "Moje rezervacije:", 400.dp) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    mojeRezervacije.forEach { rezervacija ->
                        IzbrisiRezervacijo(rezervacija.id, rezervacija.toString(), naKlik = {
                            Rezervacije.skenslaj(rezervacija.id)

                            coroutineScope.launch(Dispatchers.IO) {
                                mojeRezervacije = Rezervacije.prihodnjeRezervacije().toList()
                            }
                        })
                    }
                }
            }

        }

    } else {
        ZacetnoNalaganje()
    }

}
