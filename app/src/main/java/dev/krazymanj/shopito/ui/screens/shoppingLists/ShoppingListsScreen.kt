package dev.krazymanj.shopito.ui.screens.shoppingLists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import dev.krazymanj.shopito.ui.elements.modal.ShoppingListModalSheet
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

    var showSheet by remember { mutableStateOf(false) }
    var selectedListId by remember { mutableStateOf<String?>(null) }

    val hideSheet = {
        showSheet = false
        selectedListId = null
    }

    if (showSheet) {
        ShoppingListModalSheet(
            shoppingListId = selectedListId,
            onDismissRequest = {
                showSheet = false
                selectedListId = null
            },
            onAfterRemove = hideSheet,
            onAfterSave = { id ->
                if (selectedListId == null) {
                    navRouter.navigateTo(Destination.ViewShoppingList(id))
                }
                hideSheet()
            }
        )
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
        ShoppingListScreenContent(
            paddingValues = it,
            state = state.value,
            onShoppingListNav = { id ->
                navRouter.navigateTo(Destination.ViewShoppingList(shoppingListId = id))
            },
            onNewShoppingList = {
                showSheet = true
                selectedListId = null
            },
            onShoppingListEdit = { id ->
                showSheet = true
                selectedListId = id
            }
        )
    }
}

@Composable
fun ShoppingListScreenContent(
    paddingValues: PaddingValues,
    state: ShoppingListsUIState,
    onShoppingListNav: (id: String) -> Unit,
    onShoppingListEdit: (id: String) -> Unit,
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
                        onShoppingListNav(it.id)
                    },
                    onLongClick = {
                        onShoppingListEdit(it.id)
                    }
                )
            }
        }
    }
}
