package dev.krazymanj.shopito.views.shoppingListsSummary

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ShoppingListsSummaryViewModel @Inject constructor(private val repository: IShopitoLocalRepository) : ViewModel(),
    ShoppingListsSummaryActions {

    private val _state : MutableStateFlow<ShoppingListsSummaryUIState> = MutableStateFlow(value = ShoppingListsSummaryUIState())

    val shoppingListsSummaryUIState = _state.asStateFlow()
}