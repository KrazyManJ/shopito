package dev.krazymanj.shopito.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFFD53A3A)


val LightTextPrimary = Color(0xFF2A2A2A)
val LightTextSecondary = Color(0xFF5E5E5E)
val LightBackgroundPrimary = Color(0xFFF2F0ED)
val LightBackgroundSecondary = Color(0xFFDAD8D4)


@Composable
fun textPrimaryColor(): Color = if (isSystemInDarkTheme()) LightTextPrimary else LightTextPrimary
@Composable
fun textSecondaryColor(): Color = if (isSystemInDarkTheme()) LightTextSecondary else LightTextSecondary
@Composable
fun backgroundPrimaryColor(): Color = if (isSystemInDarkTheme()) LightBackgroundPrimary else LightBackgroundPrimary
@Composable
fun backgroundSecondaryColor(): Color = if (isSystemInDarkTheme()) LightBackgroundSecondary else LightBackgroundSecondary