package dev.krazymanj.shopito.ui.screens.shoppingLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    ShoppingListActions {

    private val _state : MutableStateFlow<ShoppingListsUIState> = MutableStateFlow(value = ShoppingListsUIState())

    val state = _state.asStateFlow()

    override fun loadLists() {
        viewModelScope.launch {
            repository.getShoppingListsByActivity().collect {
                _state.value = _state.value.copy(
                    loading = false,
                    lists = it
                )
            }
        }
    }
}