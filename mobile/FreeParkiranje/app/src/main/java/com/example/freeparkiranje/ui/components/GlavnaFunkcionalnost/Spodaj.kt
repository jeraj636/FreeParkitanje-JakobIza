package com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.freeparkiranje.R

@Composable
fun Gumb(naKlik: (() -> Unit), napis: String, modifier: Modifier, aktivaen: Boolean,slicica: Int,alt: Int) {
    val vsebina = if(!aktivaen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            contentColor = vsebina,
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    )
    {
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = vsebina
            )) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = naKlik)) {
                    Icon(
                        painter = painterResource(slicica),
                        contentDescription = stringResource(alt),
                        tint = vsebina
                    )
                    Text(napis)
                }
            }
        }
    }
}

@Composable
fun Spodaj(naKlikRPM: (() -> Unit), naKlikAvti: (() -> Unit), naKlikRezervaijca: (() -> Unit), naKlikPogovor: (()-> Unit), aktivnez: String) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            content = {}, modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .background(color = MaterialTheme.colorScheme.tertiary)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            val modifier = Modifier
                .height(50.dp)
                .weight(1f)

            Gumb(naKlikRPM, "Parkirišče", modifier, aktivnez.equals("RPM"),R.drawable.parking,R.string.Parkirisce)
            Gumb(naKlikAvti, "Avti", modifier, aktivnez.equals("Avto"),R.drawable.avto,R.string.avto)
            Gumb(naKlikRezervaijca,"Rezervacije",modifier,aktivnez.equals("Rezervacija"),R.drawable.zaslon,R.string.Rezervacije)
            Gumb(naKlikPogovor,"Pogovor",modifier,aktivnez.equals("Pog"),R.drawable.pogovor,R.string.pog)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = MaterialTheme.colorScheme.tertiary)
        ) {}
    }
}

