package dev.krazymanj.shopito.ui.elements.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.communication.CommunicationResult
import dev.krazymanj.shopito.communication.IGeoReverseRepository
import dev.krazymanj.shopito.core.IRecentLocationsManager
import dev.krazymanj.shopito.core.snackbar.SnackbarManager
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.SavedLocation
import dev.krazymanj.shopito.model.network.GeoReverseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val MAX_OPTIONS = 5

@HiltViewModel
class ShoppingItemModalSheetViewModel @Inject constructor(
    private val repository: IShopitoLocalRepository,
    private val dataStore: IDataStoreRepository,
    private val geoReverseRepository: IGeoReverseRepository,
    private val recentLocationsManager: IRecentLocationsManager,
    private val snackbarManager: SnackbarManager
) : ViewModel(), IRecentLocationsManager by recentLocationsManager {
    private val _state : MutableStateFlow<ShoppingItemModalSheetUIState> = MutableStateFlow(value = ShoppingItemModalSheetUIState())

    val state = _state.asStateFlow()

    fun loadData(shoppingItem: ShoppingItem, shoppingList: ShoppingList?) {
        viewModelScope.launch {
            _state.update { it.copy(
                item = shoppingItem,
                list = shoppingList
            ) }
            dataStore.getFlow(DataStoreKey.LastFiveLocations).collect { locations ->
                _state.update { it.copy(
                    placesOptions = locations,
                    loading = false
                ) }
            }
        }
    }

    fun updateItem(newItem: ShoppingItem) {
        _state.value = _state.value.copy(
            item = newItem.copy(
                id = _state.value.item.id,
                listId = _state.value.item.listId
            )
        )
    }

    fun save() {
        viewModelScope.launch {
            repository.update(_state.value.item)
        }
    }

    fun remove() {
        viewModelScope.launch {
            repository.delete(_state.value.item)
            snackbarManager.showDeletedItem(_state.value.item) {
                repository.upsert(it)
            }
        }
    }

    fun reset() {
        _state.value = ShoppingItemModalSheetUIState()
    }

    fun updateItemLocation(location: Location) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                geoReverseRepository.reverse(location)
            }
            when (result) {
                is CommunicationResult.ConnectionError, is CommunicationResult.Error, is CommunicationResult.Exception -> {
                    return@launch
                }
                is CommunicationResult.Success<GeoReverseResponse> -> {
                    val data = result.data
                    if (data.error != null) {
                        return@launch
                    }

                    val loc = Location(data.lat!!, data.lon!!)
                    updateItem(_state.value.item.copy(location = loc))
                    addToRecentLocations(SavedLocation(
                        label = data.displayName!!,
                        location = loc
                    ))
                }
            }
        }
    }
}