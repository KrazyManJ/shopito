package dev.krazymanj.shopito.ui.screens.scanShoppingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanShoppingListViewModel @Inject constructor(
    private val repository: IShopitoLocalRepository
) : ViewModel(), ScanShoppingListActions {
    private val _state : MutableStateFlow<ScanShoppingListUIState> = MutableStateFlow(value = ScanShoppingListUIState())

    val state = _state.asStateFlow()
    override fun loadShoppingListData(shoppingListId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = false,
                shoppingList = repository.getShoppingListById(shoppingListId)
            ) }
        }
    }

    override fun onTextScanned(text: String) {
        _state.update { it.copy(scannedText = text) }
    }

    override fun addScannedItemsToShoppingList() {
        viewModelScope.launch {
            _state.value.scannedTextToShoppingItems().forEach { repository.insert(it) }
            _state.update { it.copy(hasAddedItems = true) }
        }
    }

    fun reset() {
        _state.update { ScanShoppingListUIState() }
    }
}