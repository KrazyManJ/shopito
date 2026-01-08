package dev.krazymanj.shopito.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.krazymanj.shopito.model.SavedLocation
import dev.krazymanj.shopito.navigation.StartDestinationSetting
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.SetSerializer

sealed class DataStoreKey<T>() {
    abstract val key: Preferences.Key<*>
    abstract val default: T

    data class Primitive<T>(
        override val key: Preferences.Key<T>,
        override val default: T
    ) : DataStoreKey<T>()

    data class NullablePrimitive<T>(
        override val key: Preferences.Key<*>,
        override val default: T? = null
    ) : DataStoreKey<T?>()

    data class Object<T>(
        val name: String,
        override val default: T,
        val serializer: KSerializer<T>
    ) : DataStoreKey<T>() {
        override val key = stringPreferencesKey(name)
    }

    companion object {
        val GoogleMapsStartNavigationKey = Primitive(
            key = booleanPreferencesKey("google_maps_start_navigation_key"),
            default = false
        )

        val LastFiveLocations = Object(
            name = "last_five_locations",
            default = linkedSetOf(),
            serializer = SetSerializer(SavedLocation.serializer())
        )

        val StartScreenSetting = Object(
            name = "start_screen_setting",
            default = StartDestinationSetting.default,
            serializer = StartDestinationSetting.serializer()
        )

        val StartShoppingListId = NullablePrimitive<String?>(
            key = stringPreferencesKey("start_shopping_list_id")
        )

        val Token = NullablePrimitive<String?>(
            key = stringPreferencesKey("token")
        )
    }
}