package dev.krazymanj.shopito.views.soppingListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ShoppingListViewViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    ShoppingListViewActions {

    private val _state : MutableStateFlow<ShoppingListViewUIState> = MutableStateFlow(value = ShoppingListViewUIState())

    val templateUIState = _state.asStateFlow()

    override fun loadShoppingListData(shoppingListId: Long) {
        viewModelScope.launch {
            combine(
                repository.getShoppingListById(shoppingListId),
                repository.getShoppingItemsByShoppingList(shoppingListId)
            ) { shoppingList, shoppingItems ->
                shoppingList to shoppingItems
            }.collect { (shoppingList, shoppingItems) ->
                _state.value = _state.value.copy(
                    shoppingList = shoppingList,
                    shoppingItems = shoppingItems
                )
            }
        }
    }

    override fun addMockupShoppingItem() {
        viewModelScope.launch {
            repository.insert(ShoppingItem(
                itemName = listOf("Banana", "Orange", "Apple", "Milk").random(),
                isDone = false,
                listId = _state.value.shoppingList?.id,
                amount = Random.nextInt(1,10)
            ))
            repository.getShoppingItemsByShoppingList(_state.value.shoppingList?.id!!).collect {
                _state.value = _state.value.copy(
                    shoppingItems = it
                )
            }
        }
    }
}