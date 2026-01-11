package dev.krazymanj.shopito.core

import androidx.annotation.StringRes
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.communication.CommunicationResult
import dev.krazymanj.shopito.communication.IShopitoRemoteRepository
import dev.krazymanj.shopito.database.ShopitoDao
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.model.mappers.toEntity
import dev.krazymanj.shopito.model.mappers.toNetworkModel
import dev.krazymanj.shopito.model.network.SyncRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    private val dataStore: IDataStoreRepository,
    private val shopitoDao: ShopitoDao,
    private val repository: IShopitoRemoteRepository
) {

    sealed class Result {
        data object Success : Result()
        data class Error(@field:StringRes val errorId: Int) : Result()
        data object Unauthorized : Result()
    }

    suspend fun sync(): Result {
        val lastSync = dataStore.get(DataStoreKey.LastSyncTime) ?: 0

        val dirtyLists = shopitoDao.getDirtyLists()
        val dirtyItems = shopitoDao.getDirtyItems()

        val request = SyncRequest(
            lastSyncTimestamp = lastSync,
            lists = dirtyLists.map { it.toNetworkModel() },
            items = dirtyItems.map { it.toNetworkModel() }
        )

        val token = dataStore.get(DataStoreKey.Token) ?: return Result.Error(R.string.not_logged_in)

        val result = withContext(Dispatchers.IO) {
            repository.sync(token, request)
        }

        return when (result) {
            is CommunicationResult.ConnectionError -> Result.Error(R.string.error_no_internet_connection)
            is CommunicationResult.Error -> when (result.error.code) {
                401 -> Result.Unauthorized
                else -> Result.Error(R.string.error_unknown)
            }
            is CommunicationResult.Exception -> Result.Error(R.string.error_unknown)
            is CommunicationResult.Success -> {
                val data = result.data

                shopitoDao.markListsAsSynced(dirtyLists.map { it.id })
                shopitoDao.markItemsAsSynced(dirtyItems.map { it.id })

                shopitoDao.upsertLists(data.lists.map { it.toEntity() })
                shopitoDao.upsertItems(data.items.map { it.toEntity() })

                dataStore.set(DataStoreKey.LastSyncTime, data.newSyncTimestamp)

                shopitoDao.pruneGlobally()

                Result.Success
            }
        }
    }
}