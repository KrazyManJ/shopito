package dev.krazymanj.shopito.ui.screens.login


sealed class AuthType {
    data object Login : AuthType()
    data object Register : AuthType()

    fun switch() = when (this) {
        is Login -> Register
        is Register -> Login
    }
}

data class AuthUIState(
    val authType: AuthType = AuthType.Login,

    val usernameInput: String = "",
    val passwordInput: String = "",

    val isAuthenticating: Boolean = false,
    val error: Int? = null,

    val isAuthenticated: Boolean = false
) {
    fun isFormValid() = usernameInput.isNotBlank() && passwordInput.isNotBlank()
}
