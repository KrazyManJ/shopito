package dev.krazymanj.shopito.views.shoppingListsSummary

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen
import dev.krazymanj.shopito.ui.components.PrettyTimeText
import dev.krazymanj.shopito.ui.components.ShoppingItem

@Composable
fun ShoppingListsSummaryScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<ShoppingListsSummaryViewModel>()

    val state = viewModel.shoppingListsSummaryUIState.collectAsStateWithLifecycle()

    if (state.value.loading) {
        viewModel.loadData()
    }

    BaseScreen(
        topBarText = stringResource(R.string.navigation_lists_summary_label),
        navigationRouter = navRouter,
        showBottomNavigationBar = true
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
    LazyColumn(
        Modifier.padding(paddingValues)
    ) {

        state.shoppingItemsWithDate.forEach { (buyTime,shoppingItems) ->
            item {
                PrettyTimeText(buyTime)
            }
            items(shoppingItems) { shoppingItemWithList ->
                ShoppingItem(
                    shoppingItem = shoppingItemWithList.item,
                    modifier = Modifier.fillMaxWidth(),
                    shoppingList = shoppingItemWithList.list
                )
            }
        }

        if (state.shoppingItemsWithoutDate.isNotEmpty()) {
            item {
                Text(
                    text = "No Buy Time"
                )
            }
            items(state.shoppingItemsWithoutDate) {
                ShoppingItem(
                    it.item,
                    modifier = Modifier.fillMaxWidth(),
                    shoppingList = it.list,
                )
            }
        }
    }
}
