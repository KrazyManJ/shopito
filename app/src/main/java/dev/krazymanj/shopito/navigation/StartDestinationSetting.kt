package dev.krazymanj.shopito.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class StartDestinationSetting {
    @Serializable
    data object ShoppingSummaryScreen : StartDestinationSetting()
    @Serializable
    data object ShoppingListScreen : StartDestinationSetting()

    companion object {
        val default = ShoppingSummaryScreen

        val entries = listOf(
            ShoppingSummaryScreen,
            ShoppingListScreen
        )
    }
}