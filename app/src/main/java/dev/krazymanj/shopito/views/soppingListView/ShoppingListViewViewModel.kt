package dev.krazymanj.shopito.views.soppingListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    ShoppingListViewActions {

    private val _state : MutableStateFlow<ShoppingListViewUIState> = MutableStateFlow(value = ShoppingListViewUIState())

    val templateUIState = _state.asStateFlow()

    override fun loadShoppingListData(shoppingListId: Long) {
        viewModelScope.launch {
            repository.getShoppingItemsByShoppingList(shoppingListId).collect {
                _state.value = _state.value.copy(
                    shoppingList = repository.getShoppingListById(shoppingListId),
                    shoppingItems = it
                )
            }
        }
    }

    override fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.delete(shoppingItem)
            _state.value.shoppingList?.id?.let {
                repository.getShoppingItemsByShoppingList(it).collect { items ->
                    _state.value = _state.value.copy(
                        shoppingItems = items
                    )
                }
            }
        }
    }
}