package com.example.gatherersmap.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColors = lightColorScheme(
    primary = Color(0xff14899c), // Оранжевый
    onPrimary = Color.White,
    primaryContainer = Color(0xffd7f9fa),
    onPrimaryContainer = Color(0xff14899c),
    inversePrimary = Color(0xff232b33),
    secondary = Color(0xff54d0ff), // Синий
    onSecondary = Color.White,
    secondaryContainer = Color(0xffe9fcff),
    onSecondaryContainer = Color(0xff54d0ff),
    tertiary = Color(0xffe16945), // Красный
    onTertiary = Color.White,
    tertiaryContainer = Color(0xffffd5cc),
    onTertiaryContainer = Color(0xffe16945),
    error = Color(0xffd12027), // Красный
    onError = Color.White,
    errorContainer = Color(0xffffd5cc),
    onErrorContainer = Color(0xffd12027),
    background = Color(0xfff9fcff), // Белый
    onBackground = Color(0xff232b33),
    surface = Color(0xffe9fcff),
    onSurface = Color(0xff232b33),
    inverseSurface = Color(0xff232b33),
    inverseOnSurface = Color(0xfff9fcff),
    surfaceVariant = Color(0xffe9fcff),
    onSurfaceVariant = Color(0xff232b33),
    outline = Color(0xff607d8b)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xff336772), // Оранжевый
    onPrimary = Color(0xff232b33),
    primaryContainer = Color(0xff54d0ff),
    onPrimaryContainer = Color(0xff336772),
    inversePrimary = Color(0xff14899c),
    secondary = Color(0xff4196ff), // Синий
    onSecondary = Color(0xff232b33),
    secondaryContainer = Color(0xffe16945),
    onSecondaryContainer = Color(0xff4196ff),
    tertiary = Color(0xffb32820), // Красный
    onTertiary = Color(0xff232b33),
    tertiaryContainer = Color(0xffe16945),
    onTertiaryContainer = Color(0xffb32820),
    error = Color(0xffd12027), // Красный
    onError = Color(0xff232b33),
    errorContainer = Color(0xffe16945),
    onErrorContainer = Color(0xffd12027),
    background = Color(0xff232b33), // Черный
    onBackground = Color(0xfff9fcff),
    surface = Color(0xffe16945),
    onSurface = Color(0xfff9fcff),
    inverseSurface = Color(0xfff9fcff),
    inverseOnSurface = Color(0xff232b33),
    surfaceVariant = Color(0xffe16945),
    onSurfaceVariant = Color(0xfff9fcff),
    outline = Color(0xff607d8b)
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val useDynamicColors = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colors = when {
        useDynamicColors && useDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        useDynamicColors && !useDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        useDarkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}