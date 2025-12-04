package dev.krazymanj.shopito.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    companion object {
        const val SPLASH_SCREEN_LOADING_TIME: Long = 500
    }

    private val _state : MutableStateFlow<MainActivityUIState> = MutableStateFlow(value = MainActivityUIState())

    val mainActivityUIState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(SPLASH_SCREEN_LOADING_TIME)
            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }
}