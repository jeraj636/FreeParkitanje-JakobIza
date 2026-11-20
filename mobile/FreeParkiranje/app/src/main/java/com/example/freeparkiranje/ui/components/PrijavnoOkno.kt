package com.example.freeparkiranje.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freeparkiranje.prijavniSistem.PrijavniSistem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PrijavnoOkno(con: Context, obUspesniPrijavi: () -> Unit) {
    var upIme by remember { mutableStateOf("") }
    var upGeslo by remember { mutableStateOf("") }

    var napaka by remember { mutableStateOf<String?>(null) }
    var nalaganje by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Pozdravljen uporabnik!",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = upIme,
            onValueChange = { upIme = it },
            label = { Text("Uporabnisko ime") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), singleLine = true
        )
        OutlinedTextField(
            value = upGeslo,
            onValueChange = { upGeslo = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            singleLine = true
        )
        Button(
            onClick = {
                scope.launch {
                    nalaganje = true
                    napaka = null
                    try {
                        val uspeh = withContext(Dispatchers.IO) {
                            PrijavniSistem.prijavaSPodatki(con, upIme, upGeslo)
                        }
                        if (uspeh) {
                            obUspesniPrijavi()
                        } else {
                            napaka = "Napačno uporabniško ime ali geslo"
                        }
                    } catch (e: Exception) {
                        napaka = "Napaka pri povezavi"
                        println(e.message.toString())
                    } finally {
                        nalaganje = false
                    }

                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), enabled = !nalaganje
        ) { Text(if (nalaganje) "Prijava ..." else "Prijava") }
        if (napaka != null) {
            Text(text = napaka.toString())
        }
    }
}

