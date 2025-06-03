package dev.krazymanj.shopito.views.addEditShoppingList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen

@Composable
fun AddEditShoppingListScreen(
    navRouter: INavigationRouter,
    shoppingListId: Long?
) {
    val viewModel = hiltViewModel<AddEditShoppingListViewModel>()

    val state = viewModel.addEditShoppingListUIState.collectAsStateWithLifecycle()

    if (state.value.isDone) {
        navRouter.returnBack()
    }
    if (state.value.loading) {
        viewModel.loadShoppingListData(shoppingListId)
    }

    BaseScreen(
        topBarText = stringResource(
            if (shoppingListId != null)
                R.string.edit_shopping_list_title
            else
                R.string.add_shopping_list_title
        ),
        navigationRouter = navRouter,
        onBackClick = {
            navRouter.returnBack()
        }
    ) {
        AddEditShoppingListScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel
        )
    }
}

@Composable
fun AddEditShoppingListScreenContent(
    paddingValues: PaddingValues,
    state: AddEditShoppingUIState,
    actions: AddEditShoppingListAction
) {
    Column(
        modifier = Modifier.padding(paddingValues).padding(8.dp)
    ) {
        OutlinedTextField(
            value = state.nameInput,
            onValueChange = {
                actions.onNameInput(it)
            },
            label = { Text(stringResource(R.string.name_label)+"*") }
        )
        OutlinedTextField(
            value = state.descriptionInput,
            onValueChange = {
                actions.onDescriptionInput(it)
            },
            label = { Text(stringResource(R.string.description_label)+"*") }
        )
        Button(
            onClick = {
                actions.submit()
            }
        ) {
            Text(stringResource(R.string.save_label))
        }
    }

}
