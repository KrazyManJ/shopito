package dev.krazymanj.shopito.views.shoppingLists

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen

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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.mockupAdd()
                }
            ) {
                Icon(imageVector = Lucide.Plus, contentDescription = "add", modifier = Modifier.size(32.dp))
            }
        }
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
    state: ShoppingListsUIState,
    actions: ShoppingListActions
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues).padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.lists.forEach {
            item {
                Card (
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(it.name)
                        Text(it.description)
                    }
                }
            }
        }
    }
}
