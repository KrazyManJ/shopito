package dev.krazymanj.shopito.datastore

interface IDataStoreRepository {
    suspend fun <T> get(key: DataStoreKeys<T>): T
    suspend fun <T> set(key: DataStoreKeys<T>, value: T)
}