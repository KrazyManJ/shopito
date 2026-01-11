package dev.krazymanj.shopito.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.navigation.StartDestinationSetting
import dev.krazymanj.shopito.ui.elements.AccountCard
import dev.krazymanj.shopito.ui.elements.AppVersionString
import dev.krazymanj.shopito.ui.elements.BorderedTitle
import dev.krazymanj.shopito.ui.elements.input.SuggestionField
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing64


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
    Box(
        modifier = Modifier.fillMaxSize().padding(spacing16).padding(bottom = spacing16)
    ) {
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(spacing64)
        ) {
            AccountCard(
                loggedData = state.loggedData,
                isSyncing = state.isSyncing,
                onSyncRequest = {
                    actions.attemptSync()
                },
                syncError = state.syncError,
                lastTimeSynced = state.lastTimeSynced,
                onAuthActionRequest = {
                    if (state.isLoggedIn()) {
                        actions.logout()
                    } else {
                        navRouter.navigateTo(Destination.AuthScreen)
                    }
                }
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing32)
            ) {
                BorderedTitle(
                    title = {
                        Text(text = stringResource(R.string.settings_title))
                    }
                )
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
                    labelProvider = { stringResource( when (it) {
                        StartDestinationSetting.ShoppingListScreen -> R.string.shopping_list_title
                        StartDestinationSetting.ShoppingSummaryScreen -> R.string.navigation_lists_summary_label
                    }) },
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
        }
        AppVersionString(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
