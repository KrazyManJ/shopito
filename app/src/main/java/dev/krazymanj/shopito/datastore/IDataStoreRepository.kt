package dev.krazymanj.shopito.datastore

interface IDataStoreRepository {
    suspend fun <T> get(key: DataStoreKey<T>): T
    suspend fun <T> set(key: DataStoreKey<T>, value: T)
}