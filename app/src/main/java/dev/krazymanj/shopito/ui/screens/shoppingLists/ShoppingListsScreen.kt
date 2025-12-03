package dev.krazymanj.shopito.ui.screens.shoppingLists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.NewShoppingList
import dev.krazymanj.shopito.ui.elements.ShopitoNavigationBar
import dev.krazymanj.shopito.ui.elements.ShoppingList
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.theme.spacing16

@Composable
fun ShoppingListsScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<ShoppingListsViewModel>()

    val state = viewModel.state.collectAsStateWithLifecycle()

    if (state.value.loading){
        viewModel.loadLists()
    }

    BaseScreen(
        topBarText = stringResource(R.string.navigation_shopping_lists_label),
        bottomBar = { ShopitoNavigationBar(navRouter) },
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
        TemplateScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel,
            onShoppingListNav = { id ->
                navRouter.navigateTo(Destination.ViewShoppingList(shoppingListId = id))
            },
            onNewShoppingList = {
                navRouter.navigateTo(Destination.AddEditShoppingList(null))
            }
        )
    }
}

@Composable
fun TemplateScreenContent(
    paddingValues: PaddingValues,
    state: ShoppingListsUIState,
    actions: ShoppingListActions,
    onShoppingListNav: (id: Long) -> Unit,
    onNewShoppingList: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues).padding(spacing16),
        verticalArrangement = Arrangement.spacedBy(spacing16)
    ) {
        item {
            NewShoppingList(onClick = onNewShoppingList)
        }
        state.lists.forEach {
            item {
                ShoppingList(
                    shoppingList = it,
                    onClick = {
                        it.id?.let {
                            onShoppingListNav(it)
                        }
                    }
                )
            }
        }
    }
}
