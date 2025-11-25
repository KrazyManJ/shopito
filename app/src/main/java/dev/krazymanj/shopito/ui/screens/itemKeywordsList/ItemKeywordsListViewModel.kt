package dev.krazymanj.shopito.ui.screens.itemKeywordsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ItemKeyword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemKeywordsListViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    ItemKeywordsListActions {

    private val _state : MutableStateFlow<ItemKeywordsListUIState> = MutableStateFlow(value = ItemKeywordsListUIState())

    val itemKeywordsListUIState = _state.asStateFlow()

    override fun loadItemKeywords() {
        viewModelScope.launch {
            repository.getAllItemKeywords().collect {
                _state.value = _state.value.copy(
                    itemKeywords = it
                )
            }
        }
    }

    override fun deleteItemKeyword(itemKeyword: ItemKeyword) {
        viewModelScope.launch {
            repository.delete(itemKeyword)
        }
    }
}