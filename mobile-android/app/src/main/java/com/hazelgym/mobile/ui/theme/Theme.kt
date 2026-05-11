package com.hazelgym.mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFFF4D2E),
    secondary = Color(0xFF2266FF),
    tertiary = Color(0xFF22CC66),
    background = Color(0xFFF2F5F2),
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color(0xFF101828),
    onSurface = Color(0xFF101828)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFF4D2E),
    secondary = Color(0xFF2266FF),
    tertiary = Color(0xFF22CC66),
    background = Color(0xFF0D0D14),
    surface = Color(0xFF171923),
    onPrimary = Color.White
)

@Composable
fun HazelGymTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
