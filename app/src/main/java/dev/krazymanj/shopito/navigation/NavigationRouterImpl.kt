package dev.krazymanj.shopito.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
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
}