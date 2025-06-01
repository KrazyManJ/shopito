package dev.krazymanj.shopito.views.shoppingListsSummary

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen

@Composable
fun ShoppingListsSummaryScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<ShoppingListsSummaryViewModel>()

    val state = viewModel.shoppingListsSummaryUIState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = "Lists Summary",
        navigationRouter = navRouter
    ) {
        ShoppingListsSummaryScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel
        )
    }
}

@Composable
fun ShoppingListsSummaryScreenContent(
    paddingValues: PaddingValues,
    state: ShoppingListsSummaryUIState,
    actions: ShoppingListsSummaryActions
) {

}
