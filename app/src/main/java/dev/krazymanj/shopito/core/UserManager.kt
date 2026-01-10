package dev.krazymanj.shopito.core

import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.communication.CommunicationResult
import dev.krazymanj.shopito.communication.IShopitoRemoteRepository
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.model.TokenData
import dev.krazymanj.shopito.model.network.RegisterForm
import dev.krazymanj.shopito.model.network.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val repository: IShopitoRemoteRepository,
    private val dataStore: IDataStoreRepository
) {

    suspend fun isLoggedIn(): Boolean {
        return dataStore.get(DataStoreKey.Token) != null
    }

    sealed class AuthResponse {
        data object Success : AuthResponse()
        data class Error(val messageResId: Int) : AuthResponse()
    }

    private suspend fun processTokenResponse(
        errorCodeResolver: (Int) -> Int = { R.string.error_unknown },
        request: suspend () -> CommunicationResult<TokenResponse>,
    ): AuthResponse {
        val result = withContext(Dispatchers.IO) { request() }

        return when (result) {
            is CommunicationResult.ConnectionError -> {
                AuthResponse.Error(R.string.error_no_internet_connection)
            }
            is CommunicationResult.Error -> {
                AuthResponse.Error(errorCodeResolver(result.error.code))
            }
            is CommunicationResult.Exception -> {
                AuthResponse.Error(R.string.error_unknown)
            }
            is CommunicationResult.Success -> {
                dataStore.set(DataStoreKey.Token, result.data.accessToken)
                AuthResponse.Success
            }
        }
    }

    suspend fun login(username: String, password: String): AuthResponse {
        return processTokenResponse(
            errorCodeResolver = { when (it) {
                401 -> R.string.error_invalid_credentials
                else -> R.string.error_unknown
            }}
        ) {
            repository.login(username, password)
        }
    }

    suspend fun register(registerForm: RegisterForm): AuthResponse {
        return processTokenResponse(
            errorCodeResolver = { when (it) {
                409 -> R.string.error_user_already_exists
                else -> R.string.error_unknown
            } }
        ) {
            repository.register(registerForm)
        }
    }

    suspend fun getUserInfo(): TokenData?  {
        if (!isLoggedIn()) {
            return null
        }

        val token = dataStore.get(DataStoreKey.Token)

        if (token == null) return null

        return TokenDecoder.decodeToken(token)
    }

    suspend fun logout() {
        dataStore.set(DataStoreKey.Token, null)
        dataStore.set(DataStoreKey.LastSyncTime, null)
    }
}