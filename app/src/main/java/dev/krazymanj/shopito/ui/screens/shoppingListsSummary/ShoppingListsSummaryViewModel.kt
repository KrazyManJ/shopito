package dev.krazymanj.shopito.ui.screens.shoppingListsSummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.core.snackbar.SnackbarManager
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.model.ShoppingItemWithList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsSummaryViewModel @Inject constructor(
    private val repository: IShopitoLocalRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel(), ShoppingListsSummaryActions {

    private val _state = MutableStateFlow(value = ShoppingListsSummaryUIState())

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
            snackbarManager.showDeletedItem(shoppingItem = shoppingItem) {
                repository.upsert(it)
            }
        }
    }

    override fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean) {
        viewModelScope.launch {
            repository.upsert(shoppingItem.copy(
                isDone = state
            ))
        }
    }

    override fun setCurrentViewingShoppingItem(shoppingItem: ShoppingItemWithList?) {
        _state.value = _state.value.copy(
            currentShownShoppingItem = shoppingItem
        )
    }
}