package dev.krazymanj.shopito.views.shoppingLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    ShoppingListActions {

    private val _state : MutableStateFlow<ShoppingListsUIState> = MutableStateFlow(value = ShoppingListsUIState())

    val shoppingListsUIState = _state.asStateFlow()
    override fun mockupAdd() {
        viewModelScope.launch {
            repository.insert(ShoppingList("Example Shopping List","This is Example Shopping List"))
            repository.getShoppingLists().collect {
                _state.value = _state.value.copy(
                    lists = it
                )
            }
        }
    }

    override fun loadLists() {
        viewModelScope.launch {
            repository.getShoppingLists().collect {
                _state.value = _state.value.copy(
                    loading = false,
                    lists = it
                )
            }
        }
    }
}