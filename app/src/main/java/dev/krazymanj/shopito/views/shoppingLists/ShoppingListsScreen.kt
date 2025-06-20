package dev.krazymanj.shopito.views.shoppingLists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.Settings
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen
import dev.krazymanj.shopito.ui.components.NewShoppingList
import dev.krazymanj.shopito.ui.components.ShoppingList
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8

@Composable
fun ShoppingListsScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<ShoppingListsViewModel>()

    val state = viewModel.shoppingListsUIState.collectAsStateWithLifecycle()

    if (state.value.loading){
        viewModel.loadLists()
    }

    BaseScreen(
        topBarText = stringResource(R.string.navigation_shopping_lists_label),
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
