package dev.krazymanj.shopito.views.addEditShoppingItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ItemKeyword
import dev.krazymanj.shopito.model.Location
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
                ),
            )
        }
        else {
            viewModelScope.launch {
                val item = repository.getShoppingItemById(shoppingItemId)
                _state.value = _state.value.copy(
                    shoppingItem = item,
                    amountInput = item.amount.toString()
                )
            }
        }
        viewModelScope.launch {
            repository.getAllItemKeywords().collect {
                _state.value = _state.value.copy(
                    itemKeywords = it
                )
            }
        }
    }

    override fun onItemNameChange(itemName: String) {
        _state.value = _state.value.copy(
            shoppingItem = _state.value.shoppingItem.copy(
                itemName = itemName
            ),
            nameInputError = when {
                itemName.isEmpty() -> R.string.error_empty_input
                else -> null
            }
        )
    }

    override fun onAmountChange(amount: String) {
        _state.value = _state.value.copy(
            amountInput = amount,
            amountInputError = when {
                amount.isEmpty() -> R.string.error_empty_input
                amount.toIntOrNull() == null -> R.string.error_not_a_number
                else -> null
            }
        )
    }

    override fun onBuyTimeChanged(buyTime: Long?) {
        _state.value = _state.value.copy(
            shoppingItem = _state.value.shoppingItem.copy(
                buyTime = buyTime
            )
        )
    }

    override fun submit() {
        if (!_state.value.isInputValid()) {
            onItemNameChange(_state.value.shoppingItem.itemName)
            onAmountChange(_state.value.amountInput)
            return
        }
        viewModelScope.launch {
            val item = _state.value.shoppingItem.copy(
                amount = _state.value.amountInput.toInt()
            )
            if (_state.value.shoppingItem.isInDatabase()) {
                repository.update(item)
            }
            else {
                repository.insert(item)
            }
            repository.insert(ItemKeyword(item.itemName))
            _state.value = _state.value.copy(
                isSaved = true
            )
        }
    }

    override fun onLocationChanged(location: Location?) {
        _state.value = _state.value.copy(
            shoppingItem = _state.value.shoppingItem.copy(
                location = location
            )
        )
    }
}