package dev.krazymanj.shopito.ui.screens.settings

import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.navigation.StartDestinationSetting

interface SettingsActions {
    fun loadSettings()
    fun onStartNavigationSettingChange(value: Boolean)
    fun onStartScreenSettingChange(value: StartDestinationSetting)
    fun onStartShoppingListChange(value: ShoppingList)
    fun logout(wipeData: Boolean = false)
    fun attemptSync()
}