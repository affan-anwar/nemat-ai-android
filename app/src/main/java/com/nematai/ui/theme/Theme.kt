package com.nematai.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Purple = Color(0xFF534AB7)
private val DarkBg = Color(0xFF0F0F1A)
private val DarkSurface = Color(0xFF1A1A2E)

private val DarkColors = darkColorScheme(
    primary = Purple,
    onPrimary = Color.White,
    background = DarkBg,
    surface = DarkSurface,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF26215C),
    onSurfaceVariant = Color(0xFFCECBF6)
)

@Composable
fun NematTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography(),
        content = content
    )
}