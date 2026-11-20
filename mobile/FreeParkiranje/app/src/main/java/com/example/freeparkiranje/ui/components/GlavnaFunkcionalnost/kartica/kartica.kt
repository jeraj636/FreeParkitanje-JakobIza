package com.example.freeparkiranje.ui.components.GlavnaFunkcionalnost.kartica

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Kartica(naslov: String,visina: Dp, vsebina: @Composable (() -> Unit)) {
    Card(
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.tertiary),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(visina)

    ) {
        Column(Modifier.padding((16.dp))) {
            Row {
                Text(
                    text = naslov,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            vsebina()

        }
    }

}
