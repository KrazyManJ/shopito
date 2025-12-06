package dev.krazymanj.shopito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.core.usecase.GetLocationLabelUseCase
import dev.krazymanj.shopito.core.usecase.ReverseGeoResult
import dev.krazymanj.shopito.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GeoReverseUIState(
    val isLoading: Boolean = true,
    val error: Int? = null,
    val lastProcessedLocation: Location? = null,
    val locationLabel: String? = null,
)

@HiltViewModel
class GeoReverseViewModel @Inject constructor(
    private val getLocationLabelUseCase: GetLocationLabelUseCase
) : ViewModel() {
    private val _state : MutableStateFlow<GeoReverseUIState> = MutableStateFlow(value = GeoReverseUIState())

    val state = _state.asStateFlow()

    fun reverse(location: Location){
        if (_state.value.lastProcessedLocation == location && _state.value.error == null) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(
                lastProcessedLocation = location,
                isLoading = true
            ) }
            val result = getLocationLabelUseCase(location)

            when (result) {
                is ReverseGeoResult.Success -> {
                    _state.update { it.copy(
                        isLoading = false,
                        locationLabel = result.label,
                        error = null
                    ) }
                }
                is ReverseGeoResult.Error -> {
                    _state.update { it.copy(
                        isLoading = false,
                        error = result.messageResId,
                        locationLabel = null
                    ) }
                }
            }
        }
    }
}