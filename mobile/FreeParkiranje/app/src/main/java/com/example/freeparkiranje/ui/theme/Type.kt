package com.example.freeparkiranje.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.example.freeparkiranje.R

@OptIn(ExperimentalTextApi::class)
val playWriteFontFamily = FontFamily(
    Font(
        resId = R.font.nunito,
        weight = FontWeight.Light,
        variationSettings = FontVariation.Settings(FontVariation.weight(300))
    ),
    Font(
        resId = R.font.nunito,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400))
    ),
    Font(
        resId = R.font.nunito,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500))
    ),
    Font(
        resId = R.font.nunito,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700))
    )
)

private val defaultTypography = Typography()

val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = playWriteFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = playWriteFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = playWriteFontFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = playWriteFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = playWriteFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = playWriteFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = playWriteFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = playWriteFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = playWriteFontFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = playWriteFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = playWriteFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = playWriteFontFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = playWriteFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = playWriteFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = playWriteFontFamily)
)