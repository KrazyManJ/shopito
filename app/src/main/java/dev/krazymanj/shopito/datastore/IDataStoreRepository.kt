package dev.krazymanj.shopito.datastore

import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {
    suspend fun <T> get(key: DataStoreKeys<T>): Flow<T>
    suspend fun <T> set(key: DataStoreKeys<T>, value: T)
}