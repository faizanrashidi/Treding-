package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PolishBluePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = Color.White,
    secondary = TradeCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF0C2B3A),
    onSecondaryContainer = TradeCyan,
    tertiary = SignalBuyGreen,
    background = TradeBackgroundDark,
    surface = TradeCardDark,
    surfaceVariant = TradeCardSurface,
    outline = TradeCardBorderDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    error = SignalSellRed,
    errorContainer = SignalSellContainer,
    onError = Color.White,
    onErrorContainer = SignalSellRed
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    secondary = Color(0xFF0284C7),
    tertiary = Color(0xFFD97706),
    background = TradeBackgroundLight,
    surface = TradeSurfaceLight,
    surfaceVariant = TradeCardLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight,
    error = Color(0xFFDC2626)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek financial dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

