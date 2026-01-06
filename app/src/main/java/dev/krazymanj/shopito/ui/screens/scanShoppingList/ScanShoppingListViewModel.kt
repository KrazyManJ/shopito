package dev.krazymanj.shopito.ui.screens.scanShoppingList

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.core.ShoppingListAnalyzer
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanShoppingListViewModel @Inject constructor(
    private val repository: IShopitoLocalRepository,
    private val shoppingListAnalyzer: ShoppingListAnalyzer
) : ViewModel(), ScanShoppingListActions {
    private val _state : MutableStateFlow<ScanShoppingListUIState> = MutableStateFlow(value = ScanShoppingListUIState())

    val state = _state.asStateFlow()
    override fun loadShoppingListData(shoppingListId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = false,
                shoppingList = repository.getShoppingListById(shoppingListId)
            ) }
        }
    }

    override fun onImageCaptured(bitmap: Bitmap) {
        viewModelScope.launch {
            _state.update { it.copy(
                isScanning = true
            ) }

            val analyzeResult = shoppingListAnalyzer.analyzeImage(bitmap, state.value.shoppingList!!)

            _state.update {
                when (analyzeResult) {
                    ShoppingListAnalyzer.Result.Empty -> it.copy(
                        isScanning = false,
                        scanError = R.string.scan_result_empty
                    )
                    ShoppingListAnalyzer.Result.Error -> it.copy(
                        isScanning = false,
                        scanError = R.string.scan_result_error
                    )
                    ShoppingListAnalyzer.Result.NotAList -> it.copy(
                        isScanning = false,
                        scanError = R.string.scan_result_not_a_list
                    )
                    is ShoppingListAnalyzer.Result.Success -> it.copy(
                        scannedItems = analyzeResult.items,
                        isScanning = false,
                    )
                }
            }
        }
    }

    override fun addScannedItemsToShoppingList() {
        viewModelScope.launch {
            Log.i("Test", "Add items to shopping list")
            _state.value.scannedItems.forEach { repository.insert(it) }
            _state.update { it.copy(
                hasAddedItems = true
            ) }
        }
    }

    fun reset() {
        _state.update { ScanShoppingListUIState() }
    }
}