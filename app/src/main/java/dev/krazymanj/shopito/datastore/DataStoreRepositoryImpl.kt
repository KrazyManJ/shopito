package dev.krazymanj.shopito.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {

    override suspend fun <T> get(key: DataStoreKeys<T>): T {
        val preferences = context.dataStore.data.first()
        return preferences[key.key] ?: key.default
    }

    override suspend fun <T> set(key: DataStoreKeys<T>, value: T) {
        Log.i("Datastore", "key ${key.key.name} set to $value")
        context.dataStore.edit { preferences ->
            preferences[key.key] = value
        }
    }
}