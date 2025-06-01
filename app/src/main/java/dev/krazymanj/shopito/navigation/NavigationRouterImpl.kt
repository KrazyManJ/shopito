package dev.krazymanj.shopito.navigation

import androidx.navigation.NavController

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
}