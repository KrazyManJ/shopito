package dev.krazymanj.shopito.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    override suspend fun <T> get(key: DataStoreKey<T>): T {
        val preferences = context.dataStore.data.first()
        return when (key) {
            is DataStoreKey.Primitive -> {
                preferences[key.key] ?: key.default
            }
            is DataStoreKey.Object -> {
                val jsonString = preferences[key.key]
                if (jsonString == null) {
                    key.default
                } else {
                    try {
                        json.decodeFromString(key.serializer, jsonString)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        key.default
                    }
                }
            }
        }
    }

    override suspend fun <T> set(key: DataStoreKey<T>, value: T) {
        context.dataStore.edit { preferences ->
            when (key) {
                is DataStoreKey.Primitive -> {
                    key.key.let { prefKey ->
                        preferences[prefKey] = value
                    }
                }
                is DataStoreKey.Object -> {
                    val jsonString = json.encodeToString(key.serializer, value)
                    preferences[key.key] = jsonString
                }
            }
        }
    }
}