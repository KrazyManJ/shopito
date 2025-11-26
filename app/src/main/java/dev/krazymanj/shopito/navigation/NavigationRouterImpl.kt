package dev.krazymanj.shopito.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.squareup.moshi.Moshi
import kotlin.reflect.KClass

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    private val moshi: Moshi = Moshi.Builder().build()

    override fun navigateTo(destination: Destination) {
        navController.navigate(destination)
    }

    override fun returnBack() {
        if (!navController.popBackStack()) {
            // go to default screen if returned too much while navigating
            navController.navigate(Destination.ShoppingListsSummaryScreen)
        }
    }

    override fun isCurrentRouteOfClass(destinationClass: KClass<out Destination>): Boolean {
        val curDest = navController.currentDestination ?: return false
        return curDest.hasRoute(destinationClass)
    }

    private fun isNativeType(value: Any?): Boolean {
        return value == null || value is Int || value is String || value is Boolean ||
                value is Float || value is Long || value is Double || value is android.os.Parcelable
    }

    private fun isNativeClass(clazz: Class<*>): Boolean {
        return clazz == Int::class.java || clazz == String::class.java ||
                clazz == Boolean::class.java || android.os.Parcelable::class.java.isAssignableFrom(clazz)
    }

    override fun <T> setPreviousState(key: NavStateKey<T>, value: T) {
        val handle = navController.previousBackStackEntry?.savedStateHandle ?: return
        if (isNativeType(value)) {
            handle[key.key] = value
        }
        else {
            val json = moshi.adapter<T>(value!!::class.java).toJson(value)
            handle[key.key] = json
        }
    }

    override fun <T> getCurrentState(
        key: NavStateKey<T>,
        clazz: Class<T>
    ): T? {
        val handle = navController.currentBackStackEntry?.savedStateHandle ?: return null

        val rawValue = handle.get<String>(key.key) ?: return null

        if (!isNativeClass(clazz)) {
            return moshi.adapter(clazz).fromJson(rawValue)
        }
        @Suppress("UNCHECKED_CAST")
        return rawValue as? T
    }

    override fun clearCurrentState(key: NavStateKey<*>) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.set<Any>(key.key, null)
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.remove<Any>(key.key)
    }
}