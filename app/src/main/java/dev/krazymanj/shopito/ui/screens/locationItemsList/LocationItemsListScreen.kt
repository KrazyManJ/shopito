package dev.krazymanj.shopito.ui.screens.locationItemsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinX
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.ShoppingItem
import dev.krazymanj.shopito.ui.elements.modal.ShoppingItemModalSheet
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.elements.screen.PlaceholderScreenContent
import dev.krazymanj.shopito.ui.theme.Emphasized
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing4
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@Composable
fun LocationItemsListScreen(
    navRouter: INavigationRouter,
    route: Destination.LocationItemsList
) {
    val viewModel = hiltViewModel<LocationItemsListViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadData(route.location)
    }

    state.currentShownShoppingItem?.let {
        ShoppingItemModalSheet(
            shoppingItem = it.item,
            shoppingList = it.list,
            onDismissRequest = {
                viewModel.openShoppingItemDetails(null)
            },
            navRouter = navRouter,
            onAfterRemove = {},
            onShoppingListLinkClick = {
                it.list?.id?.let { listId ->
                    navRouter.navigateTo(Destination.ViewShoppingList(listId))
                }
            }
        )
    }

    BaseScreen(
        topBarText = stringResource(R.string.shopping_items_in_shop_title),
        onBackClick = { navRouter.returnBack() },
        showLoading = state.isLoading,
        placeholderScreenContent = if (state.items.isEmpty()) PlaceholderScreenContent(
            icon = Lucide.MapPinX,
            title = stringResource(R.string.no_items_in_shop_title)
        ) else null
    ) {
        LocationItemsListScreenContent(
            paddingValues = it,
            state = state,
            actions = viewModel
        )
    }
}

@Composable
fun LocationItemsListScreenContent(
    paddingValues: PaddingValues,
    state: LocationItemsListUIState,
    actions: LocationItemsListActions
) {

    Column(
        modifier = Modifier.padding(paddingValues).padding(spacing16),
        verticalArrangement = Arrangement.spacedBy(spacing32)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Current shop",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                color = textSecondaryColor()
            )
            Spacer(modifier = Modifier.padding(vertical = spacing4))
            Text(
                text = state.locationLabel ?: "Loading...",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Emphasized,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        LazyColumn{
            items(items = state.items, key = { it.item.id }) {
                ShoppingItem(
                    it.item,
                    shoppingList = it.list,
                    onCheckStateChange = { state ->
                        actions.changeItemCheckState(it.item, state)
                    },
                    onRemove = {
                        actions.deleteShoppingItem(it.item)
                    },
                    onClick = {
                        actions.openShoppingItemDetails(it)
                    }
                )
            }
        }
    }

}