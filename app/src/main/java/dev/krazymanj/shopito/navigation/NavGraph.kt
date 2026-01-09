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
import dev.krazymanj.shopito.ui.screens.locationItemsList.LocationItemsListScreen
import dev.krazymanj.shopito.ui.screens.login.AuthScreen
import dev.krazymanj.shopito.ui.screens.mapLocationPicker.MapLocationPickerScreen
import dev.krazymanj.shopito.ui.screens.mapView.MapViewScreen
import dev.krazymanj.shopito.ui.screens.scanShoppingList.ScanShoppingListScreen
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
            AddEditShoppingListScreen(navRouter, it.toRoute())
        }
        composable<Destination.ViewShoppingList> {
            ShoppingListViewScreen(navRouter, it.toRoute())
        }
        composable<Destination.MapLocationPickerScreen>(
            typeMap = mapOf(
                typeOf<Location?>() to serializableNavType<Location?>(isNullableAllowed = true),
                typeOf<NavStateKey<Location>>() to serializableNavType<NavStateKey<Location>>(isNullableAllowed = false),
            )
        ) {
            MapLocationPickerScreen(navRouter, it.toRoute())
        }
        composable<Destination.SettingsScreen> {
            SettingsScreen(navRouter)
        }
        composable<Destination.MapViewScreen> {
            MapViewScreen(navRouter)
        }
        composable<Destination.LocationItemsList>(
            typeMap = mapOf(
                typeOf<Location>() to serializableNavType<Location>(isNullableAllowed = false),
            )
        ) {
            LocationItemsListScreen(navRouter, it.toRoute())
        }
        composable<Destination.ScanShoppingListScreen> {
            ScanShoppingListScreen(navRouter, it.toRoute())
        }
        composable<Destination.AuthScreen> {
            AuthScreen(navRouter)
        }
    }
}