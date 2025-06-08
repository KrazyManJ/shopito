package dev.krazymanj.shopito.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFFD53A3A)

// LIGHT THEME
val LightTextPrimary = Color(0xFF1c1c1c)
val LightTextSecondary = Color(0xFF525252)
val LightBackgroundPrimary = Color(0xFFEAEAEA)
val LightBackgroundSecondary = Color(0xFFCDCDCD)

// DARK THEME
val DarkTextPrimary = Color(0xFFCDCDCD)
val DarkTextSecondary = Color(0xFFA5A5A5)
val DarkBackgroundPrimary = Color(0xFF1c1c1c)
val DarkBackgroundSecondary = Color(0xFF232323)

@Composable
fun textPrimaryColor(): Color = if (isSystemInDarkTheme()) DarkTextPrimary else LightTextPrimary
@Composable
fun textSecondaryColor(): Color = if (isSystemInDarkTheme()) DarkTextSecondary else LightTextSecondary
@Composable
fun backgroundPrimaryColor(): Color = if (isSystemInDarkTheme()) DarkBackgroundPrimary else LightBackgroundPrimary
@Composable
fun backgroundSecondaryColor(): Color = if (isSystemInDarkTheme()) DarkBackgroundSecondary else LightBackgroundSecondary