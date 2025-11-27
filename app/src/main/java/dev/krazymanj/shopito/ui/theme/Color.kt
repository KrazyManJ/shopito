package dev.krazymanj.shopito.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFFD53A3A)


val LightTextPrimary = Color(0xFF2A2A2A)
val LightTextSecondary = Color(0xFF5E5E5E)
val LightBackgroundPrimary = Color(0xFFF2F0ED)
val LightBackgroundSecondary = Color(0xFFDAD8D4)


val DarkTextPrimary = Color(0xFFF2F0ED)
val DarkTextSecondary = Color(0xFFDAD8D4)
val DarkBackgroundPrimary = Color(0xFF2A2A2A)
val DarkBackgroundSecondary = Color(0xFF5E5E5E)

@Composable
fun textPrimaryColor(): Color = if (isSystemInDarkTheme()) DarkTextPrimary else LightTextPrimary
@Composable
fun textSecondaryColor(): Color = if (isSystemInDarkTheme()) DarkTextSecondary else LightTextSecondary
@Composable
fun backgroundPrimaryColor(): Color = if (isSystemInDarkTheme()) DarkBackgroundPrimary else LightBackgroundPrimary
@Composable
fun backgroundSecondaryColor(): Color = if (isSystemInDarkTheme()) DarkBackgroundSecondary else LightBackgroundSecondary
