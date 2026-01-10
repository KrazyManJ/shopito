package dev.krazymanj.shopito.core.snackbar

import androidx.compose.material3.SnackbarDuration
import dev.krazymanj.shopito.ui.UiText

data class SnackbarMessage(
    val message: UiText,
    val actionLabel: UiText? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onAction: (suspend () -> Unit)? = null
)