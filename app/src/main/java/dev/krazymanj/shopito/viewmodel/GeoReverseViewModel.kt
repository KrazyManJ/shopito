package dev.krazymanj.shopito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.communication.CommunicationResult
import dev.krazymanj.shopito.communication.IGeoReverseRepository
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.model.GeoReverseResponse
import dev.krazymanj.shopito.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class GeoReverseUIState(
    val isLoading: Boolean = true,
    val error: Int? = null,
    val lastProcessedLocation: Location? = null,
    val locationLabel: String? = null,
)

@HiltViewModel
class GeoReverseViewModel @Inject constructor(
    private val repository: IGeoReverseRepository,
    private val dataStore: IDataStoreRepository
) : ViewModel() {
    private val _state : MutableStateFlow<GeoReverseUIState> = MutableStateFlow(value = GeoReverseUIState())

    val state = _state.asStateFlow()

    fun reverse(location: Location){
        if (_state.value.lastProcessedLocation == location) {
            return
        }
        _state.update { GeoReverseUIState(
            lastProcessedLocation = location
        ) }
        viewModelScope.launch {
            val lastFiveLocations = dataStore.get(DataStoreKey.LastFiveLocations)
            val matchingSavedLocation = lastFiveLocations.firstOrNull { it.location == location }

//            matchingSavedLocation?.let { savedLocation ->
//                _state.update { it.copy(
//                    locationLabel = savedLocation.label,
//                    isLoading = false
//                ) }
//                return@launch
//            }

            val result = withContext(Dispatchers.IO) {
                repository.reverse(location)
            }

            when (result) {
                is CommunicationResult.ConnectionError -> {
                    _state.update { it.copy(
                        isLoading = false
                    ) }
                }
                is CommunicationResult.Error -> {
                    _state.update { it.copy(
                        isLoading = false
                    ) }
                }
                is CommunicationResult.Exception -> {
                    _state.update { it.copy(
                        isLoading = false
                    ) }
                }
                is CommunicationResult.Success<GeoReverseResponse> -> {
                    _state.update { it.copy(
                        locationLabel = result.data.displayName ?: result.data.error,
                        isLoading = false
                    ) }
                }
            }
        }
    }
}