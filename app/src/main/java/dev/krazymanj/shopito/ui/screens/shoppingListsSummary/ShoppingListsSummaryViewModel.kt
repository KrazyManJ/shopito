package dev.krazymanj.shopito.ui.screens.shoppingListsSummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.model.ShoppingItemWithList
import dev.krazymanj.shopito.database.entities.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsSummaryViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    ShoppingListsSummaryActions {

    private val _state : MutableStateFlow<ShoppingListsSummaryUIState> = MutableStateFlow(value = ShoppingListsSummaryUIState())

    val state = _state.asStateFlow()

    override fun loadData(withLoadingState: Boolean) {
        viewModelScope.launch {
            if (withLoadingState) {
                _state.value = _state.value.copy(
                    isLoading = true
                )
            }
            combine(
                repository.getShoppingItemsGroupedByDate(),
                repository.getShoppingItemsWithoutBuyTime()
            ) { shoppingItemsGroupedByDate, shoppingItemsWithoutBuyTime ->
                shoppingItemsGroupedByDate to shoppingItemsWithoutBuyTime
            }.collect { (shoppingItemsGroupedByDate, shoppingItemsWithoutBuyTime) ->
                _state.value = _state.value.copy(
                    shoppingItemsWithDate = shoppingItemsGroupedByDate,
                    shoppingItemsWithoutDate = shoppingItemsWithoutBuyTime,
                    isLoading = false
                )
            }
        }
    }

    override fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.delete(shoppingItem)
        }
        saveLastDeletedItem(shoppingItem)
    }

    override fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean) {
        viewModelScope.launch {
            repository.update(shoppingItem.copy(
                isDone = state
            ))
        }
    }

    override fun setCurrentViewingShoppingItem(shoppingItem: ShoppingItemWithList?) {
        _state.value = _state.value.copy(
            currentShownShoppingItem = shoppingItem
        )
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
}