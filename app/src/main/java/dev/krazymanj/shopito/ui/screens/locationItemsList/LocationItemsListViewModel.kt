package dev.krazymanj.shopito.ui.screens.locationItemsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.core.snackbar.SnackbarManager
import dev.krazymanj.shopito.core.usecase.GetLocationLabelUseCase
import dev.krazymanj.shopito.core.usecase.ReverseGeoResult
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.ShoppingItemWithList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationItemsListViewModel @Inject constructor(
    private val repository: IShopitoLocalRepository,
    private val getLocationLabelUseCase: GetLocationLabelUseCase,
    private val snackbarManager: SnackbarManager
) : ViewModel(), LocationItemsListActions {
    private var _state = MutableStateFlow(value = LocationItemsListUIState())

    val state = _state.asStateFlow()

    fun loadData(location: Location) {
        viewModelScope.launch {
            val result = getLocationLabelUseCase(location)

            when (result) {
                is ReverseGeoResult.Success -> {
                    _state.update { it.copy(
                        locationLabel = result.label
                    ) }
                }
                is ReverseGeoResult.Error -> {
                    _state.update { it.copy(
                        locationError = result.messageResId
                    ) }
                }
            }

            repository.getItemsByLocation(location).collect { items ->
                _state.update { it.copy(
                    isLoading = false,
                    items = items,
                ) }
            }
        }
    }

    override fun openShoppingItemDetails(shoppingItem: ShoppingItemWithList?) {
        _state.update { it.copy(
            currentShownShoppingItem = shoppingItem,
        ) }
    }

    override fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.delete(item)
            snackbarManager.showDeletedItem(shoppingItem = item) {
                repository.upsert(it)
            }
        }
    }

    override fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean) {
        viewModelScope.launch {
            repository.upsert(shoppingItem.copy(
                isDone = state
            ))
        }
    }
}
