package dev.krazymanj.shopito.views.addEditShoppingItem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen

@Composable
fun AddEditShoppingItemScreen(
    navRouter: INavigationRouter,
    shoppingListId: Long,
    shoppingItemId: Long?
) {
    val viewModel = hiltViewModel<AddEditShoppingItemViewModel>()

    LaunchedEffect(shoppingListId, shoppingItemId) {
        viewModel.loadShoppingListItem(shoppingListId, shoppingItemId)
    }

    val state = viewModel.templateUIState.collectAsStateWithLifecycle()

    if (state.value.isSaved) {
        navRouter.returnBack()
    }

    BaseScreen(
        topBarText = stringResource(
            if (shoppingItemId != null) R.string.edit_shopping_item_title
            else R.string.add_shopping_item_title
        ),
        navigationRouter = navRouter
    ) {
        AddEditShoppingItemScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel
        )
    }
}

@Composable
fun AddEditShoppingItemScreenContent(
    paddingValues: PaddingValues,
    state: AddEditShoppingItemState,
    actions: AddEditShoppingItemActions
) {
    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        OutlinedTextField(
            value = state.shoppingItem.itemName,
            onValueChange = {
                actions.onItemNameChange(it)
            },
            label = {
                Text(stringResource(R.string.name_label))
            }
        )
        OutlinedTextField(
            value = state.amountInput,
            onValueChange = {
                actions.onAmountChange(it)
            },
            label = {
                Text(stringResource(R.string.amount_label))
            }
        )
        Button(onClick = {
            actions.submit()
        }) {
            Text(stringResource(R.string.save_label))
        }
    }
}
