package dev.krazymanj.shopito.communication

import dev.krazymanj.shopito.model.network.GeoReverseResponse
import dev.krazymanj.shopito.model.Location

interface IGeoReverseRepository: IBaseRemoteRepository {
    suspend fun reverse(location: Location): CommunicationResult<GeoReverseResponse>
}