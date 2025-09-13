package com.example.nit3213.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8AB4F8),
    onPrimary = Color(0xFF001D35),
    primaryContainer = Color(0xFF223149),
    onPrimaryContainer = Color(0xFFD7E3FF),

    secondary = Color(0xFF03DAC6),
    onSecondary = Color(0xFF003731),
    secondaryContainer = Color(0xFF1E4A46),
    onSecondaryContainer = Color(0xFFA4F2E7),

    tertiary = Color(0xFFEAB8FF),
    onTertiary = Color(0xFF3B2247),
    tertiaryContainer = Color(0xFF51315F),
    onTertiaryContainer = Color(0xFFFAD7FF),

    background = Color(0xFF0B0F14),
    surface = Color(0xFF0F141A),
    surfaceVariant = Color(0xFF1C242E),
    onSurface = Color(0xFFE2E8F0),
    outline = Color(0xFF3B4754),
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun NIT3213Theme(
    darkTheme: Boolean = true,               // force dark by default
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Prefer dynamic dark regardless of system setting to keep your app dark
            dynamicDarkColorScheme(context)
        } else {
            if (darkTheme || isSystemInDarkTheme()) DarkColors else DarkColors
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )

    // Optional: transparent system bars with appropriate icon contrast
    val systemUi = rememberSystemUiController()
    val useDarkIcons = false // dark mode => light icons
    SideEffect {
        systemUi.setSystemBarsColor(color = Color.Transparent, darkIcons = useDarkIcons)
        systemUi.setNavigationBarColor(color = Color.Transparent, darkIcons = useDarkIcons)
    }
}
