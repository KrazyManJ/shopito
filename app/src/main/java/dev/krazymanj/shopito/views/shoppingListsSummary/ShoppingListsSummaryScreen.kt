package dev.krazymanj.shopito.views.shoppingListsSummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.ShoppingItemWithList
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen
import dev.krazymanj.shopito.ui.components.PrettyTimeText
import dev.krazymanj.shopito.ui.components.ShoppingItem
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing4

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
        showBottomNavigationBar = true,
        actions = {
            IconButton(
                onClick = {
                    navRouter.navigateTo(Destination.SettingsScreen)
                }
            ) {
                Icon(imageVector = Lucide.Settings, contentDescription = stringResource(R.string.settings_title))
            }
        }
    ) {
        ShoppingListsSummaryScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel,
            onEditButtonClick = { item ->
                navRouter.navigateTo(
                    Destination.AddEditShoppingItem(
                    shoppingListId = item.list.id!!,
                    shoppingItemId = item.item.id
                ))
            }
        )
    }
}

@Composable
fun ShoppingListsSummaryScreenContent(
    paddingValues: PaddingValues,
    state: ShoppingListsSummaryUIState,
    actions: ShoppingListsSummaryActions,
    onEditButtonClick: (ShoppingItemWithList) -> Unit
) {
    LazyColumn(
        Modifier.padding(paddingValues).padding(spacing16),
        verticalArrangement = Arrangement.spacedBy(spacing4)
    ) {

        state.shoppingItemsWithDate.onEachIndexed { index, (buyTime,shoppingItems) ->
            item {
                PrettyTimeText(
                    timeMillis = buyTime,
                    modifier = if (index == 0) Modifier else Modifier.padding(top = spacing32)
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = spacing4)
                )
            }
            items(shoppingItems) {
                ShoppingItem(
                    shoppingItem = it.item,
                    modifier = Modifier.fillMaxWidth(),
                    shoppingList = it.list,
                    onCheckStateChange = { boolVal ->
                        actions.changeItemCheckState(it.item, boolVal)
                    },
                    onEditButtonClick = {
                        onEditButtonClick(it)
                    },
                    onDeleteButtonClick = {
                        actions.deleteShoppingItem(it.item)
                    }
                )
            }
        }

        if (state.shoppingItemsWithoutDate.isNotEmpty()) {
            item {
                Text(
                    text = "No Buy Time",
                    modifier = Modifier.padding(top = spacing32)
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = spacing4)
                )
            }
            items(state.shoppingItemsWithoutDate) {
                ShoppingItem(
                    it.item,
                    modifier = Modifier.fillMaxWidth(),
                    shoppingList = it.list,
                    onCheckStateChange = { boolVal ->
                        actions.changeItemCheckState(it.item, boolVal)
                    },
                    onEditButtonClick = {
                        onEditButtonClick(it)
                    },
                    onDeleteButtonClick = {
                        actions.deleteShoppingItem(it.item)
                    }
                )
            }
        }
    }
}
