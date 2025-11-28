package dev.krazymanj.shopito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsAccessorUIState(
    val loading: Boolean = true,
    val startNavigationSetting: Boolean = DataStoreKey.GoogleMapsStartNavigationKey.default
)

@HiltViewModel
class SettingsAccessorViewModel @Inject constructor(private val dataStore: IDataStoreRepository) : ViewModel() {

    private val _state : MutableStateFlow<SettingsAccessorUIState> = MutableStateFlow(value = SettingsAccessorUIState())

    val state = _state.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                startNavigationSetting = dataStore.get(DataStoreKey.GoogleMapsStartNavigationKey),
                loading = false
            )
        }
    }
}