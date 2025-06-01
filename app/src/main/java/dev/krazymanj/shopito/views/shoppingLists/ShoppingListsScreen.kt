package dev.krazymanj.shopito.views.shoppingLists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        topBarText = "Shopping Lists",
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.mockupAdd()
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
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
                Card{
                    Column(
                        modifier = Modifier.padding(8.dp),

                    ) {
                        Text(it.name)
                        Text(it.description)
                    }
                }
            }
        }
    }
}
