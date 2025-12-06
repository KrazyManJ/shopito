package dev.krazymanj.shopito.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStore: IDataStoreRepository,
    private val repository: IShopitoLocalRepository
) : ViewModel() {

    companion object {
        const val SPLASH_SCREEN_LOADING_TIME: Long = 500
    }

    private val _state : MutableStateFlow<MainActivityUIState> = MutableStateFlow(value = MainActivityUIState())

    val state = _state.asStateFlow()

    private suspend fun getCorrectShoppingListId(): Long? {
        val dataStoreId = dataStore.get(DataStoreKey.StartShoppingListId)
        if (dataStoreId == null) return null
        val list = repository.getShoppingListById(dataStoreId)
        return list?.id
    }

    init {
        viewModelScope.launch {
            _state.update { it.copy(
                startScreenSetting = dataStore.get(DataStoreKey.StartScreenSetting),
                startShoppingListId = getCorrectShoppingListId()
            ) }
            delay(SPLASH_SCREEN_LOADING_TIME)
            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }
}