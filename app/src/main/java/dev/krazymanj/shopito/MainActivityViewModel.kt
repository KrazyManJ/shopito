package dev.krazymanj.shopito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val _state : MutableStateFlow<MainActivityUIState> = MutableStateFlow(value = MainActivityUIState())

    val mainActivityUIState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(Constants.SPLASH_SCREEN_LOADING_TIME)
            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }
}