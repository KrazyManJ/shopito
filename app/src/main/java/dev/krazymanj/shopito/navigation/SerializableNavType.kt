package dev.krazymanj.shopito.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

inline fun <reified T : Any?> serializableNavType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
): NavType<T> {
    return object : NavType<T>(isNullableAllowed = isNullableAllowed) {
        override fun get(bundle: Bundle, key: String): T? {
            val stringValue = bundle.getString(key) ?: return null
            return json.decodeFromString(serializer<T>(), stringValue)
        }

        override fun parseValue(value: String): T {
            return json.decodeFromString(serializer<T>(), Uri.decode(value))
        }

        override fun serializeAsValue(value: T): String {
            return Uri.encode(json.encodeToString(serializer<T>(), value))
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putString(key, json.encodeToString(serializer<T>(), value))
        }
    }
}