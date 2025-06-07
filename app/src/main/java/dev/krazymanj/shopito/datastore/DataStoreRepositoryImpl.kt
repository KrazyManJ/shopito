package dev.krazymanj.shopito.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {
    override suspend fun <T> get(key: DataStoreKeys<T>): Flow<T> {
        return context.dataStore.data.map {
            preferences -> preferences[key.key] ?: key.default
        }
    }

    override suspend fun <T> set(key: DataStoreKeys<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key.key] = value
        }
    }
}