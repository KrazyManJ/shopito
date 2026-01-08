package dev.krazymanj.shopito.communication

import dev.krazymanj.shopito.model.network.TokenResponse
import javax.inject.Inject

class ShopitoRemoteRepositoryImpl @Inject constructor(
    private val shopitoApi: ShopitoApi
) : IShopitoRemoteRepository {
    override suspend fun login(username: String, password: String): CommunicationResult<TokenResponse> {
        return processResponse { shopitoApi.login(username, password) }
    }
}