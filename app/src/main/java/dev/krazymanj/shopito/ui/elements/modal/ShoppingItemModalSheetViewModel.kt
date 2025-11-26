package dev.krazymanj.shopito.ui.elements.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingItemModalSheetViewModel @Inject constructor(
    private val repository: IShopitoLocalRepository
) : ViewModel() {
    private val _state : MutableStateFlow<ShoppingItemModalSheetUIState> = MutableStateFlow(value = ShoppingItemModalSheetUIState())

    val state = _state.asStateFlow()

    fun loadData(shoppingItem: ShoppingItem, shoppingList: ShoppingList?) {
        _state.value = _state.value.copy(
            item = shoppingItem,
            list = shoppingList,
            loading = false
        )
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
            _state.value = _state.value.copy(
                dismissed = true
            )
        }
    }

    fun delete() {
        viewModelScope.launch {
            repository.delete(_state.value.item)
            _state.value = _state.value.copy(
                dismissed = true
            )
        }
    }

    fun reset() {
        _state.value = ShoppingItemModalSheetUIState()
    }
}