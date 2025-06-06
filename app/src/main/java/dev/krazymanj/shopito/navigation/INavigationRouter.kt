package dev.krazymanj.shopito.navigation

import androidx.lifecycle.MutableLiveData
import kotlin.reflect.KClass

interface INavigationRouter {
    fun navigateTo(destination: Destination)
    fun returnBack()
    fun isCurrentRouteOfClass(destinationClass: KClass<out Destination>): Boolean
    fun navigateBackWithLocationData(latitude: Double?, longitude: Double?)
    fun <T> getValue(key: String): MutableLiveData<T>?
    fun <T> removeValue(key: String)
}