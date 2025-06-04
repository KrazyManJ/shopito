package dev.krazymanj.shopito.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.krazymanj.shopito.views.addEditShoppingList.AddEditShoppingListScreen
import dev.krazymanj.shopito.views.shoppingLists.ShoppingListsScreen
import dev.krazymanj.shopito.views.shoppingListsSummary.ShoppingListsSummaryScreen
import dev.krazymanj.shopito.views.soppingListView.ShoppingListViewScreen

@Composable
fun NavGraph(
    startDestination: Destination,
    navHostController: NavHostController = rememberNavController(),
    navRouter: INavigationRouter = remember { NavigationRouterImpl(navHostController) }
) {
    NavHost(
        startDestination = startDestination,
        navController = navHostController,
    ) {
        composable<Destination.ShoppingListsSummaryScreen> {
            ShoppingListsSummaryScreen(navRouter)
        }
        composable<Destination.ShoppingListsScreen> {
            ShoppingListsScreen(navRouter)
        }
        composable<Destination.AddEditShoppingList> {
            val args = it.toRoute<Destination.AddEditShoppingList>()
            AddEditShoppingListScreen(navRouter, args.shoppingListId)
        }
        composable<Destination.ViewShoppingList> {
            val args = it.toRoute<Destination.ViewShoppingList>()
            ShoppingListViewScreen(navRouter, args.shoppingListId)
        }
    }
}