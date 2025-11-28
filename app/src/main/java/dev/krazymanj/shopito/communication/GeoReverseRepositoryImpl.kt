package dev.krazymanj.shopito.communication

import dev.krazymanj.shopito.model.GeoReverseResponse
import dev.krazymanj.shopito.model.Location
import javax.inject.Inject

class GeoReverseRepositoryImpl @Inject constructor(private val api: NominatimApi) : IGeoReverseRepository {
    override suspend fun reverse(location: Location): CommunicationResult<GeoReverseResponse> {
        return processResponse { api.reverse(lat = location.latitude, lon = location.longitude) }
    }
}