package dev.krazymanj.shopito.ui.screens.settings

interface SettingsActions {
    fun loadSettings()
    fun onStartNavigationSettingChange(value: Boolean)
}