package dev.krazymanj.shopito.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.datastore.DataStoreKeys
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val datastore: IDataStoreRepository
) : ViewModel(),
    SettingsActions {

    private val _state : MutableStateFlow<SettingsUIState> = MutableStateFlow(value = SettingsUIState())

    val templateUIState = _state.asStateFlow()

    override fun loadSettings() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                startNavigationSetting = datastore.get(DataStoreKeys.GoogleMapsStartNavigationKey),
                loading = false
            )
        }
    }

    override fun onStartNavigationSettingChange(value: Boolean) {
        viewModelScope.launch {
            datastore.set(DataStoreKeys.GoogleMapsStartNavigationKey, value)
            loadSettings()
        }
    }
}