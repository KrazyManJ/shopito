package dev.krazymanj.shopito.views.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.AppVersionString
import dev.krazymanj.shopito.ui.components.BaseScreen
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing64
import dev.krazymanj.shopito.ui.theme.spacing8


@Composable
fun SettingsScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<SettingsViewModel>()

    val state = viewModel.templateUIState.collectAsStateWithLifecycle()

    if (state.value.loading) {
        viewModel.loadSettings()
    }

    BaseScreen(
        topBarText = "Settings",
        navigationRouter = navRouter,
        onBackClick = {
            navRouter.returnBack()
        }
    ) {
        SettingsScreenContent(
            paddingValues = it,
            state = state.value,
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
        modifier = Modifier.fillMaxSize().padding(spacing16).padding(bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(spacing64)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing16),
            ) {
                Text(stringResource(R.string.setting_start_navigation),modifier=Modifier.weight(1f))
                Switch(
                    checked = state.startNavigationSetting,
                    onCheckedChange = {
                        actions.onStartNavigationSettingChange(it)
                    }
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navRouter.navigateTo(Destination.ItemKeywordsListScreen)
                    },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(text = stringResource(R.string.open_shopping_items_database))
                }
            }
        }
        AppVersionString(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
