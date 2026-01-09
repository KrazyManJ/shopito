package dev.krazymanj.shopito.core

import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.communication.CommunicationResult
import dev.krazymanj.shopito.communication.IShopitoRemoteRepository
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.model.TokenData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserManager @Inject constructor(
    private val repository: IShopitoRemoteRepository,
    private val dataStore: IDataStoreRepository
) {

    suspend fun isLoggedIn(): Boolean {
        return dataStore.get(DataStoreKey.Token) != null
    }

    sealed class LoginResponse {
        data object Success : LoginResponse()
        data class Error(val messageResId: Int) : LoginResponse()
    }

    suspend fun login(username: String, password: String): LoginResponse {
        val result = withContext(Dispatchers.IO) {
            repository.login(username, password)
        }

        return when (result) {
            is CommunicationResult.ConnectionError -> {
                LoginResponse.Error(R.string.error_no_internet_connection)
            }
            is CommunicationResult.Error -> {
                LoginResponse.Error(when (result.error.code) {
                    401 -> R.string.error_invalid_credentials
                    else -> R.string.error_unknown
                })
            }
            is CommunicationResult.Exception -> {
                LoginResponse.Error(R.string.error_unknown)
            }
            is CommunicationResult.Success -> {
                dataStore.set(DataStoreKey.Token, result.data.accessToken)
                LoginResponse.Success
            }
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