package dev.krazymanj.shopito.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.krazymanj.shopito.views.shoppingLists.ShoppingListsScreen
import dev.krazymanj.shopito.views.shoppingListsSummary.ShoppingListsSummaryScreen

@Composable
fun NavGraph(
    startDestination: Destination,
    navHostController: NavHostController = rememberNavController(),
    navRouter: INavigationRouter = remember { NavigationRouterImpl(navHostController) }
) {
    NavHost(
        startDestination = startDestination.route,
        navController = navHostController
    ) {
        composable(route = Destination.ShoppingListsSummaryScreen.route) {
            ShoppingListsSummaryScreen(navRouter)
        }
        composable(route = Destination.ShoppingListsScreen.route) {
            ShoppingListsScreen(navRouter)
        }
    }
}