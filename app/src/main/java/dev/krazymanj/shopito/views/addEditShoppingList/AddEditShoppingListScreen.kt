package dev.krazymanj.shopito.views.addEditShoppingList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trash
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8

@Composable
fun AddEditShoppingListScreen(
    navRouter: INavigationRouter,
    shoppingListId: Long?
) {
    val viewModel = hiltViewModel<AddEditShoppingListViewModel>()

    LaunchedEffect(shoppingListId) {
        viewModel.loadShoppingListData(shoppingListId)
    }

    val state = viewModel.addEditShoppingListUIState.collectAsStateWithLifecycle()

    if (state.value.isDeleted) {
        navRouter.navigateTo(Destination.ShoppingListsScreen)
    }

    if (state.value.isSaved) {
        navRouter.returnBack()
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
        },
        actions = {
            shoppingListId?.let {
                IconButton(onClick = {
                    viewModel.deleteShoppingList()
                }) {
                    Icon(imageVector = Lucide.Trash, contentDescription = stringResource(R.string.remove), tint = Primary)
                }
            }
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
        modifier = Modifier.padding(paddingValues).padding(spacing16),
        verticalArrangement = Arrangement.spacedBy(spacing8)
    ) {
        OutlinedTextField(
            value = state.shoppingList.name,
            onValueChange = {
                actions.onNameInput(it)
            },
            label = { Text(stringResource(R.string.name_label)+"*") },
            isError = state.nameInputError != null,
            supportingText = {
                state.nameInputError?.let {
                    Text(stringResource(it))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.shoppingList.description,
            onValueChange = {
                actions.onDescriptionInput(it)
            },
            label = { Text(stringResource(R.string.description_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    actions.submit()
                },
                enabled = state.isInputValid(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(stringResource(R.string.save_label))
            }
        }
    }
}
