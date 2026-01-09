package dev.krazymanj.shopito.communication

import dev.krazymanj.shopito.model.network.RegisterForm
import dev.krazymanj.shopito.model.network.SyncRequest
import dev.krazymanj.shopito.model.network.SyncResponse
import dev.krazymanj.shopito.model.network.TokenResponse
import javax.inject.Inject

class ShopitoRemoteRepositoryImpl @Inject constructor(
    private val shopitoApi: ShopitoApi,
) : IShopitoRemoteRepository {
    override suspend fun login(username: String, password: String): CommunicationResult<TokenResponse> {
        return processResponse { shopitoApi.login(username, password) }
    }

    override suspend fun register(registerForm: RegisterForm): CommunicationResult<TokenResponse> {
        return processResponse { shopitoApi.register(registerForm) }
    }

    override suspend fun sync(token: String, syncRequest: SyncRequest): CommunicationResult<SyncResponse> {
        return processResponse {
            shopitoApi.sync(
                token = "Bearer $token",
                syncRequest = syncRequest
            )
        }
    }
}