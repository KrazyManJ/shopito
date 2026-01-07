package dev.krazymanj.shopito.ui.screens.login

data class LoginUIState(
    val usernameInput: String = "",
    val passwordInput: String = "",

    val isLoggingIn: Boolean = false,
    val error: Int? = null,

    val loggedIn: Boolean = false
) {
    fun isFormValid() = usernameInput.isNotBlank() && passwordInput.isNotBlank()
}
