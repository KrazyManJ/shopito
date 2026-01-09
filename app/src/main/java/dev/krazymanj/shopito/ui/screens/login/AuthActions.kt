package dev.krazymanj.shopito.ui.screens.login

interface AuthActions {
    fun onUsernameInputChange(value: String)
    fun onPasswordInputChange(value: String)
    fun onLoginClick()
    fun switchAuthType()
}