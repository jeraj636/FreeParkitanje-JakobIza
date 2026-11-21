package com.example.freeparkiranje.ui.components.pogovor

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.freeparkiranje.cudez.Cudez
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.kartica.Kartica
import com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.vsebina.Vsebina
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun VsebinaPogovor() {
    Vsebina {


        Kartica("Izberi čas", visina = 500.dp) {
            var besedilo by remember { mutableStateOf("") }
            var aliImamoRezervacijo by remember { mutableStateOf(false) }
            val coroutineScope =
                rememberCoroutineScope() // Potrebno za izvajanje IO operacij ob kliku
            var zac by remember { mutableStateOf("") }
            var kon by remember { mutableStateOf("") }
            OutlinedTextField(
                value = besedilo,
                onValueChange = { besedilo = it },
                label = { Text("Besedilo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp), singleLine = false
            )
            Button(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        Log.d("neki", "VsebinaPogovor: 1")
                        Cudez.poisciRezervacijo(besedilo)
                        Log.d("neki", "VsebinaPogovor: 2")

                        zac = Cudez.zac
                        kon = Cudez.kon
                        aliImamoRezervacijo = true
                    } catch (e: Exception) {
                        aliImamoRezervacijo = false
                        Log.d("neki", "VsebinaPogovor: ")
                    }
                }
            }) { Text("Pošlji") }
            if (aliImamoRezervacijo) {
                Text("Dokončaj rezervacijo v parkirišču!")
            }
        }
    }

}