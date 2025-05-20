package com.example.perla.ui.theme

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = YellowMainDark,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = YellowMain,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PerlaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    primaryColorKey: String = "Yellow",
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val lightColors = lightColorScheme(
        primary = getLightColorFromKey(primaryColorKey),
        secondary = PurpleGrey40,
        tertiary = Pink40
    )

    val darkColors = darkColorScheme(
        primary = getDarkColorFromKey(primaryColorKey),
        secondary = PurpleGrey80,
        tertiary = Pink80
    )

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColors
        else -> lightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

fun getLightColorFromKey(key: String): androidx.compose.ui.graphics.Color = when (key) {
    "Yellow" -> YellowMain
    "Purple" -> PurpleMain
    "Red"    -> RedMain
    "Blue"   -> BlueMain
    "Green"  -> GreenMain
    else     -> YellowMain
}

fun getDarkColorFromKey(key: String): androidx.compose.ui.graphics.Color = when (key) {
    "Yellow" -> YellowMainDark
    "Purple" -> PurpleMainDark
    "Red"    -> RedMainDark
    "Blue"   -> BlueMainDark
    "Green"  -> GreenMainDark
    else     -> YellowMainDark
}

