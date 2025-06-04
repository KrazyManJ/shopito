package dev.krazymanj.shopito.views.soppingListView

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen

@Composable
fun ShoppingListViewScreen(
    navRouter: INavigationRouter,
    shoppingListId: Long,
) {
    val viewModel = hiltViewModel<ShoppingListViewViewModel>()

    val state = viewModel.templateUIState.collectAsStateWithLifecycle()

    if (state.value.shoppingList == null) {
        viewModel.loadShoppingListData(shoppingListId)
    }

    BaseScreen(
        topBarText = if (state.value.shoppingList != null) state.value.shoppingList!!.name else "...",
        navigationRouter = navRouter,
        showBottomNavigationBar = true,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.addMockupShoppingItem()
                }
            ) {
                Icon(imageVector = Lucide.Plus, contentDescription = "add", modifier = Modifier.size(32.dp))
            }
        },
        actions = {
            IconButton(
                onClick = {

                }
            ) { }
        }
    ) {
        ShoppingListViewScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel
        )
    }
}

@Composable
fun ShoppingListViewScreenContent(
    paddingValues: PaddingValues,
    state: ShoppingListViewUIState,
    actions: ShoppingListViewActions
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues).padding(8.dp)
    ) {
        state.shoppingItems.forEach {
            item {
                Text(it.itemName)
            }
        }
    }
}
