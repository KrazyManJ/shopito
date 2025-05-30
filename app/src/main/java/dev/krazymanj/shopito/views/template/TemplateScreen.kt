package dev.krazymanj.shopito.views.template

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen

@Composable
fun TemplateScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<TemplateViewModel>()

    val state = viewModel.templateUIState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = "Template Screen"
    ) {
        TemplateScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel
        )
    }
}

@Composable
fun TemplateScreenContent(
    paddingValues: PaddingValues,
    state: TemplateUIState,
    actions: TemplateActions
) {

}
