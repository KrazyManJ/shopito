package dev.krazymanj.shopito.communication

import dev.krazymanj.shopito.model.network.TokenResponse

interface IShopitoRemoteRepository : IBaseRemoteRepository {
    suspend fun login(username: String, password: String) : CommunicationResult<TokenResponse>
}