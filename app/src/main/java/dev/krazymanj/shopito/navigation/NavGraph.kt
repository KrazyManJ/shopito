package dev.krazymanj.shopito.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.ui.screens.addEditShoppingList.AddEditShoppingListScreen
import dev.krazymanj.shopito.ui.screens.itemKeywordsList.ItemKeywordsListScreen
import dev.krazymanj.shopito.ui.screens.mapLocationPicker.MapLocationPickerScreen
import dev.krazymanj.shopito.ui.screens.settings.SettingsScreen
import dev.krazymanj.shopito.ui.screens.shoppingListView.ShoppingListViewScreen
import dev.krazymanj.shopito.ui.screens.shoppingLists.ShoppingListsScreen
import dev.krazymanj.shopito.ui.screens.shoppingListsSummary.ShoppingListsSummaryScreen
import kotlin.reflect.typeOf


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
            val route = it.toRoute<Destination.AddEditShoppingList>()
            AddEditShoppingListScreen(navRouter, route.shoppingListId)
        }
        composable<Destination.ViewShoppingList> {
            val route = it.toRoute<Destination.ViewShoppingList>()
            ShoppingListViewScreen(navRouter, route.shoppingListId)
        }
        composable<Destination.MapLocationPickerScreen>(
            typeMap = mapOf(
                typeOf<Location?>() to serializableNavType<Location?>(isNullableAllowed = true),
                typeOf<NavStateKey<Location>>() to serializableNavType<NavStateKey<Location>>(isNullableAllowed = false),
            )
        ) {
            val route = it.toRoute<Destination.MapLocationPickerScreen>()
            MapLocationPickerScreen(navRouter, route)
        }
        composable<Destination.SettingsScreen> {
            SettingsScreen(navRouter)
        }
        composable<Destination.ItemKeywordsListScreen> {
            ItemKeywordsListScreen(navRouter)
        }
    }
}