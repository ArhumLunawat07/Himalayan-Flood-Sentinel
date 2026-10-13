package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SoberWhiteColorScheme = lightColorScheme(
    primary = RadarCyan,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = WarningAmber,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF92400E),
    tertiary = ElevationPurple,
    onTertiary = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = TextPrimary,
    surface = Color(0xFFFFFFFF),
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF8FAFC),
    onSurfaceVariant = TextSecondary,
    error = DangerRed,
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = DangerRed,
    outline = BorderSubtle
)

@Composable
fun FloodScanTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SoberWhiteColorScheme,
        typography = Typography,
        content = content
    )
}
