package dev.krazymanj.shopito.views.addEditShoppingList

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
class AddEditShoppingListViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    AddEditShoppingListAction {

    private val _state : MutableStateFlow<AddEditShoppingUIState> = MutableStateFlow(value = AddEditShoppingUIState())

    val addEditShoppingListUIState = _state.asStateFlow()

    override fun onNameInput(name: String) {
        _state.value = _state.value.copy(
            nameInput = name
        )
    }

    override fun onDescriptionInput(desc: String) {
        _state.value = _state.value.copy(
            descriptionInput = desc
        )
    }

    override fun submit() {
        val input = _state.value
        viewModelScope.launch {
            repository.insert(ShoppingList(input.nameInput,input.descriptionInput))
            _state.value = _state.value.copy(
                isDone = true
            )
        }
    }

    override fun loadShoppingListData(id: Long?) {
        if (id == null) {
            _state.value = _state.value.copy(
                loading = false
            )
            return
        }
        TODO("Editing not implemented")
    }
}