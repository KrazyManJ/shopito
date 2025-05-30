package dev.krazymanj.shopito.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToShoppingListsSummaryScreen() {
        navController.navigate(Destination.ShoppingListsSummaryScreen.route)
    }

    override fun returnBack() {
        if (!navController.popBackStack())
            // go to default screen if returned too much while navigating
            navController.navigate(Destination.ShoppingListsSummaryScreen.route)
    }
}