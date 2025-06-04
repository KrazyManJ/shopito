package dev.krazymanj.shopito.views.addEditShoppingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditShoppingListViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    AddEditShoppingListAction {

    private val _state : MutableStateFlow<AddEditShoppingUIState> = MutableStateFlow(value = AddEditShoppingUIState())

    val addEditShoppingListUIState = _state.asStateFlow()

    override fun onNameInput(name: String) {
        _state.value = _state.value.copy(
            shoppingList = _state.value.shoppingList.copy(name = name)
        )
    }

    override fun onDescriptionInput(desc: String) {
        _state.value = _state.value.copy(
            shoppingList = _state.value.shoppingList.copy(description = desc)
        )
    }

    override fun submit() {
        viewModelScope.launch {
            if (_state.value.shoppingList.id != null) {
                repository.update(_state.value.shoppingList)
            }
            else {
                repository.insert(_state.value.shoppingList)
            }
            _state.value = _state.value.copy(
                isSaved = true
            )
        }
    }

    override fun loadShoppingListData(id: Long?) {
        if (id == null) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                shoppingList = repository.getShoppingListById(id),
            )
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