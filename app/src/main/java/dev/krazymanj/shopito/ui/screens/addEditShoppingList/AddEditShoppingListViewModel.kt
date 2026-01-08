package dev.krazymanj.shopito.ui.screens.addEditShoppingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditShoppingListViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    AddEditShoppingListAction {

    private val _state : MutableStateFlow<AddEditShoppingUIState> = MutableStateFlow(value = AddEditShoppingUIState())

    val addEditShoppingListUIState = _state.asStateFlow()

    override fun onNameInput(name: String) {
        _state.value = _state.value.copy(
            shoppingList = _state.value.shoppingList.copy(name = name),
            nameInputError = when {
                name.isBlank() -> R.string.error_empty_input
                else -> null
            }
        )
    }

    override fun onDescriptionInput(desc: String) {
        _state.value = _state.value.copy(
            shoppingList = _state.value.shoppingList.copy(description = desc)
        )
    }

    override fun submit() {
        if (!_state.value.isInputValid()) {
            onNameInput(_state.value.shoppingList.name)
            return
        }
        viewModelScope.launch {
            repository.upsert(_state.value.shoppingList)
            _state.value = _state.value.copy(
                isSaved = true
            )
        }
    }

    override fun loadShoppingListData(id: String?) {
        if (id == null) return

        viewModelScope.launch {
            repository.getShoppingListById(id)?.let { item ->
                _state.update { it.copy(
                    shoppingList = item,
                )}
            }
        }
    }

    override fun deleteShoppingList() {
        viewModelScope.launch {
            repository.delete(_state.value.shoppingList)
            _state.value = _state.value.copy(
                isDeleted = true
            )
        }
    }
}