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
        startDestination = startDestination,
        navController = navHostController
    ) {
        composable<Destination.ShoppingListsScreen> {
            ShoppingListsSummaryScreen(navRouter)
        }
        composable<Destination.ShoppingListsSummaryScreen> {
            ShoppingListsScreen(navRouter)
        }
    }
}