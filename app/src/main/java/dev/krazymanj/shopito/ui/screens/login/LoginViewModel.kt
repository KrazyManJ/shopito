package dev.krazymanj.shopito.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.core.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userManager: UserManager
) : ViewModel(), LoginActions {
    private var _state = MutableStateFlow(value = LoginUIState())

    val state = _state.asStateFlow()

    override fun onUsernameInputChange(value: String) {
        _state.update { it.copy(usernameInput = value) }
    }

    override fun onPasswordInputChange(value: String) {
        _state.update { it.copy(passwordInput = value) }
    }

    override fun onLoginClick() {
        viewModelScope.launch {
            _state.update { it.copy(isLoggingIn = true, error = null) }
            val result = userManager.login(
                username = state.value.usernameInput,
                password = state.value.passwordInput
            )

            when (result) {
                is UserManager.LoginResponse.Error -> {
                    _state.update { it.copy(
                        isLoggingIn = false,
                        error = result.messageResId
                    ) }
                }
                UserManager.LoginResponse.Success -> {
                    _state.update { it.copy(
                        isLoggingIn = false,
                        loggedIn = true
                    ) }
                }
            }
        }
    }
}
