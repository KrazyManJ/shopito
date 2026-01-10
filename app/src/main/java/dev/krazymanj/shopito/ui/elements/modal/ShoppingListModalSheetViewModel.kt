package dev.krazymanj.shopito.ui.elements.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListModalSheetViewModel @Inject constructor(
    private val repository: IShopitoLocalRepository
) : ViewModel() {
    private val _state : MutableStateFlow<ShoppingListModalSheetUIState> = MutableStateFlow(value = ShoppingListModalSheetUIState())

    val state = _state.asStateFlow()

    fun loadShoppingList(shoppingListId: String?) {
        viewModelScope.launch {
            val shoppingList = shoppingListId?.let { repository.getShoppingListById(it) }
            _state.update { it.copy(
                isLoading = false,
                shoppingList = shoppingList ?: _state.value.shoppingList,
                isEdit = shoppingList != null
            ) }

        }
    }

    fun onListUpdate(shoppingList: ShoppingList) {
        _state.update { it.copy(
            shoppingList = shoppingList.copy(
                id = _state.value.shoppingList.id,
                createdAt = _state.value.shoppingList.createdAt
            )
        ) }
    }

    fun reset() {
        _state.update { ShoppingListModalSheetUIState() }
    }

    fun save() {
        viewModelScope.launch {
            repository.upsert(_state.value.shoppingList)
        }
    }

    fun delete() {
        viewModelScope.launch {
            repository.delete(_state.value.shoppingList)
        }
    }
}