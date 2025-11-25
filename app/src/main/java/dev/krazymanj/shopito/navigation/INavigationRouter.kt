package dev.krazymanj.shopito.navigation

import androidx.lifecycle.MutableLiveData
import dev.krazymanj.shopito.model.Location
import kotlin.reflect.KClass

interface INavigationRouter {
    fun navigateTo(destination: Destination)
    fun returnBack()
    fun isCurrentRouteOfClass(destinationClass: KClass<out Destination>): Boolean
    fun navigateBackWithLocationData(location: Location)
    fun <T> getValue(key: String): MutableLiveData<T>?
    fun removeValue(key: String)
}