package dev.krazymanj.shopito.navigation

import kotlin.reflect.KClass

interface INavigationRouter {
    fun navigateTo(destination: Destination)
    fun returnBack()
    fun isCurrentRouteOfClass(destinationClass: KClass<out Destination>): Boolean
}