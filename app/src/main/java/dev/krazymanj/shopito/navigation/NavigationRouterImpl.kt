package dev.krazymanj.shopito.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToTemplateScreen() {
        navController.navigate(Destination.TemplateScreen.route)
    }

    override fun returnBack() {
        if (!navController.popBackStack())
            // go to default screen if returned too much while navigating
            navController.navigate(Destination.TemplateScreen.route)
    }
}