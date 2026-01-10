package dev.krazymanj.shopito.core.snackbar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalSnackbarHostStateViewModel @Inject constructor(
    snackbarManager: SnackbarManager
) : ViewModel() {
    val snackbarMessages = snackbarManager.messageEvent
}