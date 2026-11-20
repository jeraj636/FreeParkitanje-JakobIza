package com.example.freeparkiranje.ui.components.Avti


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.freeparkiranje.avti.Avti
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.kartica.Kartica
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.vsebina.Vsebina
import com.example.freeparkiranje.ui.components.ZacetnoNalaganje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun IzbrisAvta(registerska: String, ikona: Int, posebn: String, naKlik: (() -> Unit)) {
    val coroutineScope = rememberCoroutineScope() // Potrebno za izvajanje IO operacij ob kliku

    Row {
        Column(modifier = Modifier.weight(1f)) {
            Text(registerska, color = if(registerska.equals(posebn)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Card(Modifier.clickable(onClick = { coroutineScope.launch(Dispatchers.IO) { naKlik() } }),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
                Icon(
                    painter = painterResource(ikona),
                    contentDescription = stringResource(R.string.kos),
                    tint = if(registerska.equals(posebn)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
    HorizontalDivider()

}

@Composable
fun VsebinaAvti() {
    var mojiAvti by remember { mutableStateOf(emptyList<String>()) }
    var nalagam by remember { mutableStateOf(true) }
    var registerskaVhor by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope() // Potrebno za izvajanje IO operacij ob kliku

    LaunchedEffect(Unit) {
        mojiAvti = withContext(Dispatchers.IO) {
            Avti.DobiMojeAvte().toList()

        }
        nalagam = false
    }
    if (!nalagam) {
      Vsebina(){
            Kartica(naslov = "Moji avtomobili:",250.dp) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    mojiAvti.forEach { avto ->
                        IzbrisAvta(registerska = avto, posebn = "",ikona = R.drawable.kos, naKlik = {
                            Avti.IzbrisiAvto(avto)

                            coroutineScope.launch (Dispatchers.IO){ mojiAvti = Avti.DobiMojeAvte().toList() }
                        })
                    }
                }
            }
            Row(Modifier.height(15.dp)) { }
            Kartica(naslov = "Dodaj avtomobil:",200.dp) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = registerskaVhor,
                        onValueChange = { registerskaVhor = it },
                        label = { Text("Registrska") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        singleLine = true
                    )
                    Button(onClick = {coroutineScope.launch (Dispatchers.IO){
                        Avti.DodajAvto(registerskaVhor)
                        registerskaVhor=""
                        mojiAvti = Avti.DobiMojeAvte().toList()
                    }}){
                        Text("Dodaj avto")
                    }
                }
            }
        }

    } else {
        ZacetnoNalaganje()
    }


}
