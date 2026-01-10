package dev.krazymanj.shopito.core.snackbar

import androidx.compose.material3.SnackbarDuration
import dev.krazymanj.shopito.ui.UiText

data class SnackbarMessage(
    val message: UiText,
    val actionLabel: UiText? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration? = null,
    val onAction: (suspend () -> Unit)? = null,
    val reAppearOnNavigation: Boolean = false,
)