package dev.krazymanj.shopito.ui.screens.login

interface LoginActions {
    fun onUsernameInputChange(value: String)
    fun onPasswordInputChange(value: String)
    fun onLoginClick()
}