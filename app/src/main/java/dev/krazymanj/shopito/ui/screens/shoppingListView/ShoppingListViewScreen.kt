package dev.krazymanj.shopito.ui.screens.shoppingListView

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.ListX
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Menu
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.ScanText
import com.composables.icons.lucide.StickyNote
import com.composables.icons.lucide.Trash
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.navigation.NavStateKey
import dev.krazymanj.shopito.navigation.NavigationCurrentStateReceivedEffect
import dev.krazymanj.shopito.ui.elements.ShoppingItem
import dev.krazymanj.shopito.ui.elements.input.QuickAdd
import dev.krazymanj.shopito.ui.elements.modal.LocationOptionsDialog
import dev.krazymanj.shopito.ui.elements.modal.ShoppingItemModalSheet
import dev.krazymanj.shopito.ui.elements.modal.ShoppingListModalSheet
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.elements.screen.PlaceholderScreenContent
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@Composable
fun ShoppingListViewScreen(
    navRouter: INavigationRouter,
    route: Destination.ViewShoppingList
) {
    val viewModel = hiltViewModel<ShoppingListViewViewModel>()

    val state = viewModel.state.collectAsStateWithLifecycle()

    if (state.value.isLoading) {
        viewModel.loadShoppingListData(route.shoppingListId)
    }

    NavigationCurrentStateReceivedEffect(navRouter, NavStateKey.LocationQuickAddResult) { location ->
        viewModel.updateItemLocation(location)
    }

    val focusManager = LocalFocusManager.current

    var locationOptionsVisible by remember { mutableStateOf(false) }

    if (locationOptionsVisible) {
        LaunchedEffect(Unit) {
            viewModel.loadPlacesOptions()
        }
        LocationOptionsDialog(
            options = state.value.placesOptions,
            selectedLocation = state.value.locationInput,
            onDismissRequest = {
                locationOptionsVisible = false
            },
            onSelected = {
                viewModel.onLocationChanged(it.location)
            },
            onMapPickerClicked = {
                navRouter.navigateTo(Destination.MapLocationPickerScreen(
                    state.value.locationInput,
                    NavStateKey.LocationQuickAddResult
                ))
            },
            onDeleteRequest = {
                viewModel.removeFromRecentLocations(it)
            }
        )
    }

    state.value.currentShownShoppingItem?.let { shoppingItem ->
        ShoppingItemModalSheet(
            shoppingItem = shoppingItem,
            onDismissRequest = {
                viewModel.openShoppingItemDetails(null)
            },
            navRouter = navRouter,
            onAfterRemove = {}
        )
    }

    var showListEditSheet by remember { mutableStateOf(false) }

    if (showListEditSheet) {
        ShoppingListModalSheet(
            shoppingListId = route.shoppingListId,
            onDismissRequest = {
                showListEditSheet = false
            },
            onAfterRemove = {
                showListEditSheet = false
                navRouter.returnBack()
            },
            onAfterSave = {
                showListEditSheet = false
                viewModel.loadShoppingListData(route.shoppingListId)
            }
        )
    }

    var expandedMenu by remember { mutableStateOf(false) }

    BaseScreen(
        topBarText = stringResource(R.string.shopping_list_title),
        topBarTextDescription = state.value.shoppingList?.name ?: stringResource(R.string.unnamed),
        onBackClick = {
            navRouter.returnBack()
        },
        showLoading = state.value.isLoading,
        actions = {
            IconButton(
                onClick = { expandedMenu = true }
            ) {
                Icon(imageVector = Lucide.Menu, contentDescription = null)
            }
            DropdownMenu(
                expanded = expandedMenu,
                onDismissRequest = { expandedMenu = false },
                containerColor = backgroundSecondaryColor()
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.scan)) },
                    leadingIcon = { Icon(imageVector = Lucide.ScanText, contentDescription = null) },
                    onClick = {
                        expandedMenu = false
                        navRouter.navigateTo(Destination.ScanShoppingListScreen(route.shoppingListId))
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.remove_all_checked_items)) },
                    leadingIcon = { Icon(imageVector = Lucide.ListX, contentDescription = null) },
                    onClick = {
                        expandedMenu = false
                        viewModel.removeAllCheckedItems()
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.edit)) },
                    leadingIcon = { Icon(imageVector = Lucide.Pencil, contentDescription = null) },
                    onClick = {
                        expandedMenu = false
                        showListEditSheet = true
                    }
                )
                HorizontalDivider(color = textSecondaryColor())
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.remove)) },
                    colors = MenuDefaults.itemColors(
                        textColor = Primary,
                        leadingIconColor = Primary
                    ),
                    leadingIcon = { Icon(imageVector = Lucide.Trash, contentDescription = null) },
                    onClick = {
                        expandedMenu = false
                        viewModel.deleteList()
                        navRouter.returnBack()
                    }
                )
            }
        },
        placeholderScreenContent = if (state.value.shoppingItems.isEmpty()) PlaceholderScreenContent(
            icon = Lucide.StickyNote,
            title = stringResource(R.string.list_placeholder_title),
            text = stringResource(R.string.list_placeholder_text)
        ) else null,
        bottomBar = {
            QuickAdd(
                value = state.value.itemInput,
                date = state.value.dateInput,
                location = state.value.locationInput,
                onValueChange = { viewModel.onItemInput(it) },
                onAdd = { viewModel.addShoppingItem() },
                onDateChange = { viewModel.onDateInput(it) },
                onLocationChangeRequest = {
                    locationOptionsVisible = true
                },
                onLocationClearRequest = {
                    viewModel.onLocationChanged(null)
                }
            )
        },
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
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
    actions: ShoppingListViewActions,
) {
    val listState = rememberLazyListState()

    if (state.isCreated) {
        LaunchedEffect(Unit) {
            if (state.shoppingItems.size > 5) {
                listState.animateScrollToItem(state.shoppingItems.size-1)
            }
            actions.clearIsCreatedState()
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(spacing16),
        state = listState
    ) {
        items(
            items = state.shoppingItems,
            key = { item -> item.id }
        ) {
            ShoppingItem(
                shoppingItem = it,
                modifier = Modifier.fillMaxWidth(),
                onCheckStateChange = { boolVal ->
                    actions.changeItemCheckState(it, boolVal)
                },
                onClick = {
                    actions.openShoppingItemDetails(it)
                },
                onRemove = {
                    actions.deleteShoppingItem(it)
                }
            )
        }
    }
}
