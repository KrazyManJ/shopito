package dev.krazymanj.shopito.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException

@Suppress("UNCHECKED_CAST")
class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private fun <T> mapKey(preferences: Preferences, key: DataStoreKey<T>): T {
        return when (key) {
            is DataStoreKey.Primitive -> {
                preferences[key.key] as T ?: key.default
            }
            is DataStoreKey.NullablePrimitive<*> -> {
                Log.i("Test", "Nullable primitive ${preferences[key.key]}")
                preferences[key.key] as T
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

    override suspend fun <T> get(key: DataStoreKey<T>): T {
        val preferences = context.dataStore.data.first()
        return mapKey(preferences, key)
    }

    override fun <T> getFlow(key: DataStoreKey<T>): Flow<T> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences -> mapKey(preferences, key) }
            .distinctUntilChanged()
    }

    override suspend fun <T> set(key: DataStoreKey<T>, value: T) {
        context.dataStore.edit { preferences ->
            when (key) {
                is DataStoreKey.Primitive -> {
                    preferences[key.key] = value
                }
                is DataStoreKey.NullablePrimitive<*> -> {
                    val prefKey = key.key as Preferences.Key<Any>
                    if (value as T? == null) {
                        preferences.remove(prefKey)
                    } else {
                        preferences[prefKey] = value as Any
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