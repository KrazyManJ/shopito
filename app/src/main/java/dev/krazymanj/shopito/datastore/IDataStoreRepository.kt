package dev.krazymanj.shopito.datastore

import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {
    suspend fun <T> get(key: DataStoreKey<T>): T
    suspend fun <T> set(key: DataStoreKey<T>, value: T)
    fun <T> getFlow(key: DataStoreKey<T>): Flow<T>
}