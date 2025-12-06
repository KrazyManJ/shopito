package dev.krazymanj.shopito.ui.screens.mapView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewViewModel @Inject constructor(
    private val repository: IShopitoLocalRepository
) : ViewModel() {

    private var _state = MutableStateFlow(value = MapViewUIState())

    val state = _state.asStateFlow()

    fun loadLocations() {
        viewModelScope.launch {
            repository.getAllDistinctLocationsFromItems().collect { locations ->
                _state.update { it.copy(
                    isLoading = false,
                    locations = locations
                ) }
            }
        }
    }
}