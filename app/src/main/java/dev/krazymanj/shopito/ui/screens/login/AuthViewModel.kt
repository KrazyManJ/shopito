package dev.krazymanj.shopito.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.core.UserManager
import dev.krazymanj.shopito.model.network.RegisterForm
import dev.krazymanj.shopito.worker.WorkScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userManager: UserManager,
    private val workScheduler: WorkScheduler
) : ViewModel(), AuthActions {
    private var _state = MutableStateFlow(value = AuthUIState())

    val state = _state.asStateFlow()

    override fun onUsernameInputChange(value: String) {
        _state.update { it.copy(usernameInput = value) }
    }

    override fun onPasswordInputChange(value: String) {
        _state.update { it.copy(passwordInput = value) }
    }

    override fun onLoginClick() {
        viewModelScope.launch {
            _state.update { it.copy(isAuthenticating = true, error = null) }
            val result: UserManager.AuthResponse = when (_state.value.authType) {
                AuthType.Login -> userManager.login(
                    username = state.value.usernameInput,
                    password = state.value.passwordInput
                )
                AuthType.Register -> userManager.register(
                    RegisterForm(
                        username = state.value.usernameInput,
                        password = state.value.passwordInput
                    )
                )
            }

            when (result) {
                is UserManager.AuthResponse.Error -> {
                    _state.update { it.copy(
                        isAuthenticating = false,
                        error = result.messageResId
                    ) }
                }
                UserManager.AuthResponse.Success -> {
                    workScheduler.scheduleOneTimeSync()

                    workScheduler.schedulePeriodicSync()
                    _state.update { it.copy(
                        isAuthenticating = false,
                        isAuthenticated = true
                    ) }
                }
            }
        }
    }

    override fun switchAuthType() {
        _state.update { it.copy(authType = it.authType.switch()) }
    }
}
