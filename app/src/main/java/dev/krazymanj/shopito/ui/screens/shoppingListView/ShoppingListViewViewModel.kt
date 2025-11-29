package dev.krazymanj.shopito.ui.screens.shoppingListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.communication.CommunicationResult
import dev.krazymanj.shopito.communication.IGeoReverseRepository
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.extension.empty
import dev.krazymanj.shopito.extension.removeFirstItem
import dev.krazymanj.shopito.model.GeoReverseResponse
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.SavedLocation
import dev.krazymanj.shopito.ui.elements.modal.MAX_OPTIONS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewViewModel @Inject constructor(
    private val repository: IShopitoLocalRepository,
    private val dataStore: IDataStoreRepository,
    private val geoReverseRepository: IGeoReverseRepository
) : ViewModel(),
    ShoppingListViewActions {

    private val _state : MutableStateFlow<ShoppingListViewUIState> = MutableStateFlow(value = ShoppingListViewUIState())

    val templateUIState = _state.asStateFlow()

    override fun loadShoppingListData(shoppingListId: Long) {
        viewModelScope.launch {
            repository.getShoppingItemsByShoppingList(shoppingListId).collect {
                _state.value = _state.value.copy(
                    shoppingList = repository.getShoppingListById(shoppingListId),
                    shoppingItems = it,
                    isLoading = false
                )
            }
        }
    }

    override fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.delete(shoppingItem)
            saveLastDeletedItem(shoppingItem)
        }
    }

    override fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean) {
        viewModelScope.launch {
            repository.update(shoppingItem.copy(
                isDone = state
            ))
        }
    }

    fun onItemInput(itemInput: String) {
        _state.value = _state.value.copy(
           itemInput = itemInput
        )
    }

    private fun extractLastAmount(input: String): Pair<String, Int> {

        val regex = Regex("\\b(x\\d+|\\d+x)\\b", RegexOption.IGNORE_CASE)

        val matches = regex.findAll(input)

        val lastMatch = matches.lastOrNull() ?: return Pair(input, 1)

        val numberPart = lastMatch.value.replace("x", "", ignoreCase = true)
        val parsedNumber = numberPart.toIntOrNull() ?: 1

        val range = lastMatch.range

        val textBefore = input.substring(0, range.first).trim()
        val textAfter = input.substring(range.last + 1).trim()

        val cleanText = "$textBefore $textAfter".trim()

        return Pair(cleanText, parsedNumber)
    }

    fun addShoppingItem() {
        val itemInput = _state.value.itemInput
        if (itemInput.isEmpty()) {
            return
        }
        if (_state.value.shoppingList == null) {
            return
        }

        val (text, amount) = extractLastAmount(itemInput)

        viewModelScope.launch {
            repository.insert(ShoppingItem(
                itemName = text,
                amount = amount,
                buyTime = _state.value.dateInput,
                location = _state.value.locationInput,
                listId = _state.value.shoppingList!!.id
            ))
            _state.value = _state.value.copy(
                itemInput = String.empty,
                isCreated = true
            )
        }
    }

    override fun clearIsCreatedState() {
        _state.value = _state.value.copy(
            isCreated = false
        )
    }

    fun onDateInput(date: Long?) {
        _state.value = _state.value.copy(
            dateInput = date
        )
    }

    fun onLocationChanged(location: Location?) {
        _state.value = _state.value.copy(
            locationInput = location
        )
    }

    override fun openShoppingItemDetails(shoppingItem: ShoppingItem?) {
        _state.value = _state.value.copy(
            currentShownShoppingItem = shoppingItem
        )
    }

    fun loadPlacesOptions() {
        viewModelScope.launch {
            _state.update { it.copy(
                placesOptions = dataStore.get(DataStoreKey.LastFiveLocations)
            ) }
        }
    }

    private suspend fun saveToHistory(savedLocation: SavedLocation) {
        val set = dataStore.get(DataStoreKey.LastFiveLocations) as LinkedHashSet<SavedLocation>
        set.add(savedLocation)
        if (set.size > MAX_OPTIONS) {
            set.removeFirstItem()
        }
        dataStore.set(DataStoreKey.LastFiveLocations, set)
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
                    onLocationChanged(
                        location = loc
                    )
                    saveToHistory(SavedLocation(
                        label = data.displayName!!,
                        location = loc
                    ))
                }
            }
        }
    }

    fun deleteFromHistory(savedLocation: SavedLocation){
        viewModelScope.launch {
            val set = dataStore.get(DataStoreKey.LastFiveLocations) as LinkedHashSet<SavedLocation>
            set.remove(savedLocation)
            dataStore.set(DataStoreKey.LastFiveLocations, set)
            loadPlacesOptions()
        }
    }

    override fun saveLastDeletedItem(shoppingItem: ShoppingItem) {
        _state.update { it.copy(
            lastDeletedItem = shoppingItem
        ) }
    }

    override fun addBackDeletedItem() {
        viewModelScope.launch {
            _state.value.lastDeletedItem?.let {
                repository.insert(it)
            }
        }
    }

    fun removeAllCheckedItems() {
        viewModelScope.launch {
            repository.removeAllCheckedItemsInShoppingList(_state.value.shoppingList!!.id!!)
        }
    }
}