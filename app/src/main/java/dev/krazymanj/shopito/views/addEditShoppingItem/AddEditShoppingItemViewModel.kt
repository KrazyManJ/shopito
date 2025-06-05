package dev.krazymanj.shopito.views.addEditShoppingItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditShoppingItemViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    AddEditShoppingItemActions {

    private val _state : MutableStateFlow<AddEditShoppingItemState> = MutableStateFlow(value = AddEditShoppingItemState())

    val templateUIState = _state.asStateFlow()

    override fun loadShoppingListItem(shoppingListId: Long, shoppingItemId: Long?) {
        if (shoppingItemId == null) {
            _state.value = _state.value.copy(
                shoppingItem = _state.value.shoppingItem.copy(
                    listId = shoppingListId
                )
            )
        }
        else {
            TODO("Edit not implemented")
        }
    }

    override fun onItemNameChange(itemName: String) {
        _state.value = _state.value.copy(
            shoppingItem = _state.value.shoppingItem.copy(
                itemName = itemName
            )
        )
    }

    override fun onAmountChange(amount: String) {
        _state.value = _state.value.copy(
            amountInput = amount
        )
    }

    override fun submit() {
        viewModelScope.launch {
            repository.insert(_state.value.shoppingItem.copy(
                amount = _state.value.amountInput.toInt()
            ))
            _state.value = _state.value.copy(
                isSaved = true
            )
        }
    }
}