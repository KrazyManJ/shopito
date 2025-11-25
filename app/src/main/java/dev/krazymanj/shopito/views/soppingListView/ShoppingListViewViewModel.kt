package dev.krazymanj.shopito.views.soppingListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.extension.empty
import dev.krazymanj.shopito.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    ShoppingListViewActions {

    private val _state : MutableStateFlow<ShoppingListViewUIState> = MutableStateFlow(value = ShoppingListViewUIState())

    val templateUIState = _state.asStateFlow()

    private suspend fun loadItems() {
        _state.value.shoppingList?.id?.let {
            repository.getShoppingItemsByShoppingList(it).collect { items ->
                _state.value = _state.value.copy(
                    shoppingItems = items
                )
            }
        }
    }

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
            loadItems()
        }
    }

    override fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean) {
        viewModelScope.launch {
            repository.update(shoppingItem.copy(
                isDone = state
            ))
            loadItems()
        }
    }

    fun onItemInput(itemInput: String) {
        _state.value = _state.value.copy(
           itemInput = itemInput
        )
    }

    private fun extractLastAmount(input: String): Pair<String, Int> {

        val regex = Regex("\\b(x\\d+|\\d+x)\\b", RegexOption.IGNORE_CASE)

        val matches = regex.findAll(input)

        val lastMatch = matches.lastOrNull() ?: return Pair(input, 1)

        val numberPart = lastMatch.value.replace("x", "", ignoreCase = true)
        val parsedNumber = numberPart.toIntOrNull() ?: 1

        val range = lastMatch.range

        val textBefore = input.substring(0, range.first).trim()
        val textAfter = input.substring(range.last + 1).trim()

        val cleanText = "$textBefore $textAfter"

        return Pair(cleanText, parsedNumber)
    }

    fun addShoppingItem() {
        val itemInput = _state.value.itemInput
        if (itemInput.isEmpty()) {
            return
        }
        if (_state.value.shoppingList == null) {
            return
        }

        val (text, amount) = extractLastAmount(itemInput)

        viewModelScope.launch {
            repository.insert(ShoppingItem(
                itemName = text,
                amount = amount,
                buyTime = _state.value.dateInput,
                location = _state.value.locationInput,
                listId = _state.value.shoppingList!!.id
            ))
            _state.value = _state.value.copy(
                itemInput = String.empty,
                isCreated = true
            )
            loadItems()
        }
    }

    override fun clearIsCreatedState() {
        _state.value = _state.value.copy(
            isCreated = false
        )
    }

    fun onDateInput(date: Long) {
        _state.value = _state.value.copy(
            dateInput = date
        )
    }

    fun onLocationChanged(location: Location) {
        _state.value = _state.value.copy(
            locationInput = location
        )
    }
}