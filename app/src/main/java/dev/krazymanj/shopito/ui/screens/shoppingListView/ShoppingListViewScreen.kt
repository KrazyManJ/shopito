package dev.krazymanj.shopito.ui.screens.shoppingListView

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.ListX
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.StickyNote
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.extension.showDeletedMessage
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.navigation.NavStateKey
import dev.krazymanj.shopito.navigation.NavigationCurrentStateReceivedEffect
import dev.krazymanj.shopito.ui.elements.ShoppingItem
import dev.krazymanj.shopito.ui.elements.input.QuickAdd
import dev.krazymanj.shopito.ui.elements.modal.LocationOptionsDialog
import dev.krazymanj.shopito.ui.elements.modal.ShoppingItemModalSheet
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.elements.screen.PlaceholderScreenContent
import dev.krazymanj.shopito.ui.theme.spacing16
import kotlinx.coroutines.launch

@Composable
fun ShoppingListViewScreen(
    navRouter: INavigationRouter,
    shoppingListId: Long,
) {
    val viewModel = hiltViewModel<ShoppingListViewViewModel>()

    val state = viewModel.state.collectAsStateWithLifecycle()

    if (state.value.isLoading) {
        viewModel.loadShoppingListData(shoppingListId)
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

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    state.value.currentShownShoppingItem?.let { shoppingItem ->
        ShoppingItemModalSheet(
            shoppingItem = shoppingItem,
            onDismissRequest = {
                viewModel.openShoppingItemDetails(null)
            },
            navRouter = navRouter,
            onAfterRemove = {
                scope.launch {
                    snackbarHostState.showDeletedMessage(it,context) {
                        viewModel.addBackDeletedItem()
                    }
                }
            }
        )
    }

    BaseScreen(
        topBarText = if (state.value.shoppingList != null) state.value.shoppingList!!.name else "...",
        onBackClick = {
            navRouter.returnBack()
        },
        showLoading = state.value.isLoading,
        actions = {
            IconButton(
                onClick = {
                    navRouter.navigateTo(Destination.AddEditShoppingList(shoppingListId = shoppingListId))
                }
            ) {
                Icon(imageVector = Lucide.Pencil, contentDescription = null)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.removeAllCheckedItems()
                }
            ) {
                Icon(
                    imageVector = Lucide.ListX,
                    contentDescription = null
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
        snackbarHostState = snackbarHostState,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
    ) {
        ShoppingListViewScreenContent(
            paddingValues = it,
            snackbarHostState = snackbarHostState,
            state = state.value,
            actions = viewModel
        )
    }
}

@Composable
fun ShoppingListViewScreenContent(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    state: ShoppingListViewUIState,
    actions: ShoppingListViewActions,
) {
    val listState = rememberLazyListState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
            key = { item -> item.id!! }
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
                    scope.launch {
                        actions.deleteShoppingItem(it)
                        snackbarHostState.showDeletedMessage(it,context) {
                            actions.addBackDeletedItem()
                        }
                    }
                }
            )
        }
    }
}
