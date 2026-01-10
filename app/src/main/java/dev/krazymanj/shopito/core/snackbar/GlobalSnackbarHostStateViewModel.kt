package dev.krazymanj.shopito.core.snackbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalSnackbarHostStateViewModel @Inject constructor(
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    val snackbarMessages = snackbarManager.messageEvent

    fun queueMessageOnNewScreen(message: SnackbarMessage) {
        viewModelScope.launch {
            snackbarManager.showMessage(message)
        }
    }
}