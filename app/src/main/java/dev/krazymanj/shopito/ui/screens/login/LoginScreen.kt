package dev.krazymanj.shopito.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.theme.spacing16

@Composable
fun LoginScreen(
    navRouter: INavigationRouter,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.loggedIn) {
        navRouter.returnBack()
    }

    BaseScreen(
        topBarText = "Login",
        onBackClick = { navRouter.returnBack() }
    ) {
        LoginScreenContent(it, state, viewModel)
    }
}

@Composable
private fun LoginScreenContent(
    paddingValues: PaddingValues,
    state: LoginUIState,
    actions: LoginActions
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(paddingValues).padding(spacing16),
        verticalArrangement = Arrangement.spacedBy(spacing16)
    ) {
        OutlinedTextField(
            value = state.usernameInput,
            onValueChange = { actions.onUsernameInputChange(it) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.passwordInput,
            onValueChange = { actions.onPasswordInputChange(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        state.error?.let { error ->
            Text(
                text = stringResource(error),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = {
                actions.onLoginClick()
            },
            enabled = state.isFormValid(),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            if (state.isLoggingIn) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
            else {
                Text("Login")
            }
        }
    }
}