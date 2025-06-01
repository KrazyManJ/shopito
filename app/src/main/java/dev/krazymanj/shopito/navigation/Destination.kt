package dev.krazymanj.shopito.navigation

sealed class Destination(val route: String) {
    object ShoppingListsSummaryScreen : Destination(route = "template_screen")
    object ShoppingListsScreen : Destination(route = "shopping_lists")
}