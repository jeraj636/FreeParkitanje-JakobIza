package com.example.freeparkiranje.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Oranzna,
    onPrimary = Color.White,
    secondary = TemnoRjava,
    onSecondary = Color.White,
    tertiary = TemnoRjava,
    background = Krem,
    onBackground = TemnoRjava,
    surface = Color(0xFFFFFFFF), // Bela za kartice in vnosna polja
    onSurface = TemnoRjava
)

// Temna tema (prilagojena za udoben nočni način z bogatimi toni)
private val DarkColorScheme = darkColorScheme(
    primary = Oranzna,
    onPrimary = TemnoOzadje,
    secondary = Color.White,
    onSecondary = Color.White,
    tertiary = Krem,
    background = TemnoOzadje,
    onBackground = Krem,
    surface = Color(0xFF2C221A), // Nekoliko svetlejši odtenek rjave za gumbe/kartice
    onSurface = Krem
)
@Composable
fun FreeParkiranjeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}