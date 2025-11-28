package dev.krazymanj.shopito.ui.screens.shoppingListsSummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import com.composables.icons.lucide.StickyNote
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.extension.showDeletedMessage
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.PrettyTimeText
import dev.krazymanj.shopito.ui.elements.ShopitoNavigationBar
import dev.krazymanj.shopito.ui.elements.ShoppingItem
import dev.krazymanj.shopito.ui.elements.modal.ShoppingItemModalSheet
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.elements.screen.PlaceholderScreenContent
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing4
import kotlinx.coroutines.launch

@Composable
fun ShoppingListsSummaryScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<ShoppingListsSummaryViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        viewModel.loadData()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    BaseScreen(
        topBarText = stringResource(R.string.navigation_lists_summary_label),
        bottomBar = { ShopitoNavigationBar(navRouter) },
        showLoading = state.isLoading,
        placeholderScreenContent = if (state.hasNoData()) PlaceholderScreenContent(
            icon = Lucide.StickyNote,
            title = stringResource(R.string.summary_placeholder_title),
            text = stringResource(R.string.summary_placeholder_text)
        ) else null,
        actions = {
            IconButton(
                onClick = {
                    navRouter.navigateTo(Destination.SettingsScreen)
                }
            ) {
                Icon(imageVector = Lucide.Settings, contentDescription = stringResource(R.string.settings_title))
            }
        },
        snackbarHostState = snackbarHostState
    ) {
        ShoppingListsSummaryScreenContent(
            paddingValues = it,
            snackbarHostState = snackbarHostState,
            state = state,
            actions = viewModel
        )
    }

    state.currentShownShoppingItem?.let { shoppingItemWithList ->
        ShoppingItemModalSheet(
            shoppingItem = shoppingItemWithList.item,
            shoppingList = shoppingItemWithList.list,
            onDismissRequest = {
                viewModel.setCurrentViewingShoppingItem(null)
            },
            onShoppingListLinkClick = {
                viewModel.setCurrentViewingShoppingItem(null)
                navRouter.navigateTo(Destination.ViewShoppingList(shoppingListId = shoppingItemWithList.list.id!!))
            },
            navRouter = navRouter,
            onAfterRemove = {
                scope.launch {
                    viewModel.saveLastDeletedItem(it)
                    snackbarHostState.showDeletedMessage(it,context) {
                        viewModel.addBackDeletedItem()
                    }
                }
            }
        )
    }
}

@Composable
fun ShoppingListsSummaryScreenContent(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    state: ShoppingListsSummaryUIState,
    actions: ShoppingListsSummaryActions
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LazyColumn(
        Modifier
            .padding(paddingValues)
            .padding(spacing16),
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
            items(
                items = shoppingItems,
                key = { it.item.id!! }
            ) {
                ShoppingItem(
                    shoppingItem = it.item,
                    modifier = Modifier.fillMaxWidth(),
                    shoppingList = it.list,
                    onCheckStateChange = { boolVal ->
                        actions.changeItemCheckState(it.item, boolVal)
                    },
                    onClick = {
                        actions.setCurrentViewingShoppingItem(it)
                    },
                    onRemove = {
                        scope.launch {
                            actions.deleteShoppingItem(it.item)
                            snackbarHostState.showDeletedMessage(it.item, context) {
                                actions.addBackDeletedItem()
                            }
                        }
                    }
                )
            }
        }

        if (state.shoppingItemsWithoutDate.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.no_buy_time),
                    modifier = Modifier.padding(top = spacing32)
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = spacing4)
                )
            }
            items(
                items = state.shoppingItemsWithoutDate,
                key = { it.item.id!! }
            ) {
                ShoppingItem(
                    it.item,
                    modifier = Modifier.fillMaxWidth(),
                    shoppingList = it.list,
                    onCheckStateChange = { boolVal ->
                        actions.changeItemCheckState(it.item, boolVal)
                    },
                    onClick = {
                        actions.setCurrentViewingShoppingItem(it)
                    },
                    onRemove = {
                        scope.launch {
                            actions.deleteShoppingItem(it.item)
                            snackbarHostState.showDeletedMessage(it.item, context) {
                                actions.addBackDeletedItem()
                            }
                        }
                    }
                )
            }
        }
    }
}
