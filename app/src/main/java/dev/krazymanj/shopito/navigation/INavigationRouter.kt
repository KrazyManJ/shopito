package dev.krazymanj.shopito.navigation

import kotlin.reflect.KClass

interface INavigationRouter {
    fun navigateTo(destination: Destination)
    fun returnBack()
    fun isCurrentRouteOfClass(destinationClass: KClass<out Destination>): Boolean
    fun <T> setPreviousState(key: NavStateKey<T>, value: T)
    fun <T> getCurrentState(key: NavStateKey<T>, clazz: Class<T>): T?
    fun clearCurrentState(key: NavStateKey<*>)
}