package dev.krazymanj.shopito.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.theme.Emphasized
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.textPrimaryColor

@Composable
fun AuthScreen(
    navRouter: INavigationRouter,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isAuthenticated) {
        navRouter.returnBack()
    }

    BaseScreen(
        topBarText = when (state.authType) {
            AuthType.Login -> "Login"
            AuthType.Register -> "Register"
        },
        onBackClick = { navRouter.returnBack() }
    ) {
        LoginScreenContent(it, state, viewModel)
    }
}

@Composable
private fun LoginScreenContent(
    paddingValues: PaddingValues,
    state: AuthUIState,
    actions: AuthActions
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(paddingValues).padding(spacing16),
        verticalArrangement = Arrangement.spacedBy(spacing32)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing16)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing16),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.shopito_icon),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge,
                        color = textPrimaryColor(),
                        fontWeight = FontWeight.Emphasized
                    )
                }
            }
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
                if (state.isAuthenticating) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                else {
                    Text(
                        text = when (state.authType) {
                            AuthType.Login -> "Login"
                            AuthType.Register -> "Register"
                        }
                    )
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    actions.switchAuthType()
                },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = when (state.authType) {
                        AuthType.Login -> "Don't have an account? Register here"
                        AuthType.Register -> "Already have an account? Login here"
                    }
                )
            }
        }
    }
}