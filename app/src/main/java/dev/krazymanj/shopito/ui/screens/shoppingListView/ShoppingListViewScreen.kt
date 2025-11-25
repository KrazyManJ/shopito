package dev.krazymanj.shopito.ui.screens.shoppingListView

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dev.krazymanj.shopito.Constants
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.BaseScreen
import dev.krazymanj.shopito.ui.elements.QuickAdd
import dev.krazymanj.shopito.ui.elements.ShoppingItem
import dev.krazymanj.shopito.ui.theme.spacing16

@Composable
fun ShoppingListViewScreen(
    navRouter: INavigationRouter,
    shoppingListId: Long,
) {
    val viewModel = hiltViewModel<ShoppingListViewViewModel>()

    LaunchedEffect(shoppingListId) {
        viewModel.loadShoppingListData(shoppingListId)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        val mapLocationResult = navRouter.getValue<String>(Constants.LOCATION)
        mapLocationResult?.value?.let {
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Location> = moshi.adapter(Location::class.java)
            val location = jsonAdapter.fromJson(it)
            navRouter.removeValue(Constants.LOCATION)
            location?.let { loc ->
                viewModel.onLocationChanged(loc)
            }
        }
    }

    val state = viewModel.templateUIState.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    BaseScreen(
        topBarText = if (state.value.shoppingList != null) state.value.shoppingList!!.name else "...",
        onBackClick = {
            navRouter.returnBack()
        },
        actions = {
            IconButton(
                onClick = {
                    navRouter.navigateTo(Destination.AddEditShoppingList(shoppingListId = shoppingListId))
                }
            ) {
                Icon(imageVector = Lucide.Pencil, contentDescription = null)
            }
        },
        bottomBar = {
            QuickAdd(
                value = state.value.itemInput,
                date = state.value.dateInput,
                location = state.value.locationInput,

                onValueChange = { viewModel.onItemInput(it) },
                onAdd = { viewModel.addShoppingItem() },
                onDateChange = { viewModel.onDateInput(it) },
                onLocationChangeRequest = {
                    navRouter.navigateTo(Destination.MapLocationPickerScreen(
                        state.value.locationInput
                    ))
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
