package dev.krazymanj.shopito.communication

import dev.krazymanj.shopito.model.network.RegisterForm
import dev.krazymanj.shopito.model.network.SyncRequest
import dev.krazymanj.shopito.model.network.SyncResponse
import dev.krazymanj.shopito.model.network.TokenResponse

interface IShopitoRemoteRepository : IBaseRemoteRepository {
    suspend fun login(username: String, password: String) : CommunicationResult<TokenResponse>
    suspend fun register(registerForm: RegisterForm) : CommunicationResult<TokenResponse>
    suspend fun sync(token: String, syncRequest: SyncRequest) : CommunicationResult<SyncResponse>
}