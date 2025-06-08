package dev.krazymanj.shopito.views.settings

import android.content.pm.PackageInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.AppVersionString
import dev.krazymanj.shopito.ui.components.BaseScreen


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
        modifier = Modifier.fillMaxSize().padding(bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Row {
                Text("On map redirect, start navigation immediately?")
                Switch(
                    checked = state.startNavigationSetting,
                    onCheckedChange = {
                        actions.onStartNavigationSettingChange(it)
                    }
                )
            }
            Button(
                onClick = {
                    navRouter.navigateTo(Destination.ItemKeywordsListScreen)
                }
            ) {
                Text(text = "Open Item Keywords")
            }
        }
        AppVersionString(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
