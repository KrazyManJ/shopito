package dev.krazymanj.shopito.views.soppingListView

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.Plus
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen
import dev.krazymanj.shopito.ui.components.ShoppingItem

@Composable
fun ShoppingListViewScreen(
    navRouter: INavigationRouter,
    shoppingListId: Long,
) {
    val viewModel = hiltViewModel<ShoppingListViewViewModel>()

    LaunchedEffect(shoppingListId) {
        viewModel.loadShoppingListData(shoppingListId)
    }

    val state = viewModel.templateUIState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = if (state.value.shoppingList != null) state.value.shoppingList!!.name else "...",
        navigationRouter = navRouter,
        showBottomNavigationBar = true,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navRouter.navigateTo(
                        Destination.AddEditShoppingItem(
                            shoppingListId = shoppingListId,
                            shoppingItemId = null
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = Lucide.Plus,
                    contentDescription = "add",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    navRouter.navigateTo(Destination.AddEditShoppingList(shoppingListId = shoppingListId))
                }
            ) {
                Icon(imageVector = Lucide.Pencil, contentDescription = null)
            }
        }
    ) {
        ShoppingListViewScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel,
            onEditButtonClick = { item ->
                navRouter.navigateTo(Destination.AddEditShoppingItem(
                    shoppingListId = shoppingListId,
                    shoppingItemId = item.id
                ))
            }
        )
    }
}

@Composable
fun ShoppingListViewScreenContent(
    paddingValues: PaddingValues,
    state: ShoppingListViewUIState,
    actions: ShoppingListViewActions,
    onEditButtonClick: (ShoppingItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(32.dp)
    ) {
        state.shoppingItems.forEach {
            item {
                ShoppingItem(
                    shoppingItem = it,
                    modifier = Modifier.fillMaxWidth(),
                    onCheckStateChange = { boolVal ->
                        actions.changeItemCheckState(it, boolVal)
                    },
                    onEditButtonClick = {
                        onEditButtonClick(it)
                    },
                    onDeleteButtonClick = {
                        actions.deleteShoppingItem(it)
                    }
                )
            }
        }
    }
}
