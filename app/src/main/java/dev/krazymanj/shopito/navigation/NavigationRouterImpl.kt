package dev.krazymanj.shopito.navigation

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dev.krazymanj.shopito.Constants
import dev.krazymanj.shopito.model.Location
import kotlin.reflect.KClass

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
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

    override fun navigateBackWithLocationData(location: Location) {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Location> = moshi.adapter(Location::class.java)

        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(Constants.LOCATION, jsonAdapter.toJson(location))
        returnBack()
    }

    override fun <T> getValue(key: String): MutableLiveData<T>? {
        return navController.currentBackStackEntry?.savedStateHandle?.getLiveData(key)
    }

    override fun removeValue(key: String) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.remove<Any>(key)
    }
}