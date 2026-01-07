package dev.krazymanj.shopito.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.core.TokenDecoder
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.navigation.StartDestinationSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val datastore: IDataStoreRepository,
    private val repository: IShopitoLocalRepository
) : ViewModel(),
    SettingsActions {

    private val _state : MutableStateFlow<SettingsUIState> = MutableStateFlow(value = SettingsUIState())

    val state = _state.asStateFlow()

    override fun loadSettings() {
        viewModelScope.launch {
            combine(
                datastore.getFlow(DataStoreKey.GoogleMapsStartNavigationKey),
                datastore.getFlow(DataStoreKey.StartScreenSetting),
                datastore.getFlow(DataStoreKey.StartShoppingListId),
                repository.getShoppingLists(),
                datastore.getFlow(DataStoreKey.Token)
            ) { googleMapStartNavigation, startScreenSetting, startShoppingListId, shoppingLists, token ->
                SettingsUIState(
                    isLoading = false,
                    startNavigationSetting = googleMapStartNavigation,
                    startScreenSetting = startScreenSetting,
                    startShoppingListId = startShoppingListId,
                    shoppingLists = shoppingLists,
                    loggedData = TokenDecoder.decodeToken(token ?: "")
                )
            }.collect { state ->
                _state.update { state }
            }
        }
    }

    override fun onStartNavigationSettingChange(value: Boolean) {
        viewModelScope.launch {
            datastore.set(DataStoreKey.GoogleMapsStartNavigationKey, value)
        }
    }

    override fun onStartScreenSettingChange(value: StartDestinationSetting) {
        viewModelScope.launch {
            datastore.set(DataStoreKey.StartScreenSetting, value)
        }
    }

    override fun onStartShoppingListChange(value: ShoppingList) {
        viewModelScope.launch {
            datastore.set(DataStoreKey.StartShoppingListId, value.id!!)
        }
    }

    override fun logout() {
        viewModelScope.launch {
            datastore.set(DataStoreKey.Token, null)
        }
    }
}