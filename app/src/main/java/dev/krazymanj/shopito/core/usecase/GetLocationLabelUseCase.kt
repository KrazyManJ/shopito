package dev.krazymanj.shopito.core.usecase

import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.communication.CommunicationResult
import dev.krazymanj.shopito.communication.IGeoReverseRepository
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.model.Location
import javax.inject.Inject

sealed interface ReverseGeoResult {
    data class Success(val label: String) : ReverseGeoResult
    data class Error(val messageResId: Int?) : ReverseGeoResult
}

class GetLocationLabelUseCase @Inject constructor(
    private val repository: IGeoReverseRepository,
    private val dataStore: IDataStoreRepository
) {
    suspend operator fun invoke(location: Location): ReverseGeoResult{
        val lastFiveLocations = dataStore.get(DataStoreKey.LastFiveLocations)
        val matchingSavedLocation = lastFiveLocations.firstOrNull { it.location == location }

        if (matchingSavedLocation != null) {
            return ReverseGeoResult.Success(matchingSavedLocation.label)
        }

        val result = repository.reverse(location)

        return when (result) {
            is CommunicationResult.Success -> {
                val error = if (result.data.error != null) R.string.unnamed else null
                if (error != null) {
                    ReverseGeoResult.Error(error)
                } else {
                    ReverseGeoResult.Success(result.data.displayName ?: "")
                }
            }
            is CommunicationResult.ConnectionError,
            is CommunicationResult.Error,
            is CommunicationResult.Exception -> {
                ReverseGeoResult.Error(R.string.unknown)
            }
        }
    }
}