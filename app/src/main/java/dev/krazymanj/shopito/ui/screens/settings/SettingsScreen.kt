package dev.krazymanj.shopito.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.UserRound
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.navigation.StartDestinationSetting
import dev.krazymanj.shopito.ui.elements.AppVersionString
import dev.krazymanj.shopito.ui.elements.FilledCard
import dev.krazymanj.shopito.ui.elements.input.SuggestionField
import dev.krazymanj.shopito.ui.elements.modal.LogoutDialog
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing4
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor
import dev.krazymanj.shopito.utils.DateUtils


@Composable
fun SettingsScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<SettingsViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        viewModel.loadSettings()
    }

    BaseScreen(
        topBarText = stringResource(R.string.settings_title),
        showLoading = state.isLoading,
        onBackClick = {
            navRouter.returnBack()
        }
    ) {
        SettingsScreenContent(
            paddingValues = it,
            state = state,
            actions = viewModel,
            navRouter = navRouter
        )
    }
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    state: SettingsUIState,
    actions: SettingsActions,
    navRouter: INavigationRouter
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismissRequest = { showLogoutDialog = false },
            onConfirm = { wipeData ->
                showLogoutDialog = false
                actions.logout(wipeData)
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(spacing16).padding(bottom = spacing16)
    ) {
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(spacing32)
        ) {

            FilledCard(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(spacing16),
                    verticalArrangement = Arrangement.spacedBy(spacing8)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing16)
                    ) {
                        Icon(
                            imageVector = Lucide.UserRound,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                        Column {
                            Text(
                                text = state.loggedData?.username ?: "Guest",
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = when {
                                    state.lastTimeSynced != null -> "Last sync: ${DateUtils.getDateTimeString(state.lastTimeSynced)}"
                                    state.syncError != null -> state.syncError
                                    state.isSyncing -> "Syncing..."
                                    else -> ""
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = when {
                                    state.syncError != null -> Primary
                                    else -> textSecondaryColor()
                                }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth().padding(vertical = spacing4),
                        color = textSecondaryColor()
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.isLoggedIn()) {
                            TextButton(
                                onClick = {
                                    actions.attemptSync()
                                }
                            ) {
                                if (state.isSyncing) {
                                    CircularProgressIndicator()
                                }
                                else {
                                    Text("Sync")
                                }
                            }
                        }
                        Spacer(modifier = Modifier)
                        TextButton(
                            onClick = {
                                if (state.isLoggedIn()) {
                                    showLogoutDialog = true
                                } else {
                                    navRouter.navigateTo(Destination.AuthScreen)
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = textPrimaryColor())
                        ) {
                            Text(text = if (state.isLoggedIn()) "Logout" else "Login")
                        }
                    }
                    state.syncError?.let {
                        Text(text = it, color = Primary)
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing16),
            ) {
                Text(
                    text = stringResource(R.string.setting_start_navigation),
                    modifier=Modifier.weight(1f)
                )
                Switch(
                    checked = state.startNavigationSetting,
                    onCheckedChange = {
                        actions.onStartNavigationSettingChange(it)
                    }
                )
            }
            SuggestionField(
                options = StartDestinationSetting.entries,
                labelProvider = {it.toString()},
                value = state.startScreenSetting,
                onValueChange = { actions.onStartScreenSettingChange(it) },
                labelText = "When app opens...",
                modifier = Modifier.fillMaxWidth()
            )
            SuggestionField(
                options = state.shoppingLists,
                labelProvider = { it.name },
                value = state.shoppingLists.find { list -> list.id == state.startShoppingListId },
                onValueChange = { actions.onStartShoppingListChange(it) },
                labelText = "Shopping List to open",
                modifier = Modifier.fillMaxWidth(),
                enabled = state.startScreenSetting == StartDestinationSetting.ShoppingListScreen
            )
        }
        AppVersionString(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
