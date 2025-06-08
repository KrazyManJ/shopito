package dev.krazymanj.shopito.views.settings

interface SettingsActions {
    fun loadSettings()
    fun onStartNavigationSettingChange(value: Boolean)
}