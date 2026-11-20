package com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Ogrodje(
    zgoraj: @Composable (() -> Unit),
    spodaj:
    @Composable (()
    -> Unit),
    content: @Composable ((PaddingValues) -> Unit)
) {

    Scaffold(
        topBar = zgoraj,
        bottomBar = spodaj,
        modifier = Modifier
            .fillMaxSize(),

        ) { innerPadding -> content(innerPadding) }
}

