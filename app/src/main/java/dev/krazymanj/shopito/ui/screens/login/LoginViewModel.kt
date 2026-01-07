package dev.krazymanj.shopito.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.communication.CommunicationResult
import dev.krazymanj.shopito.communication.IShopitoRemoteRepository
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IShopitoRemoteRepository,
    private val datastore: IDataStoreRepository
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
            val result = withContext(Dispatchers.IO) {
                repository.login(
                    username = state.value.usernameInput,
                    password = state.value.passwordInput
                )
            }

            when (result) {
                is CommunicationResult.ConnectionError -> {
                    _state.update { it.copy(
                        isLoggingIn = false,
                        error = R.string.error_no_internet_connection
                    )}
                }
                is CommunicationResult.Error -> {
                    _state.update { it.copy(
                        isLoggingIn = false,
                        error = R.string.error_invalid_credentials
                    ) }
                }
                is CommunicationResult.Exception -> {
                    _state.update { it.copy(
                        isLoggingIn = false,
                        error = R.string.error_unknown
                    ) }
                }
                is CommunicationResult.Success -> {
                    datastore.set(DataStoreKey.Token, result.data.accessToken)
                    _state.update { it.copy(
                        isLoggingIn = false,
                        loggedIn = true
                    ) }
                }
            }
        }
    }
}
