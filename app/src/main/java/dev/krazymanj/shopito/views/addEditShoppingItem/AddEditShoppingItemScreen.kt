package dev.krazymanj.shopito.views.addEditShoppingItem

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pin
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dev.krazymanj.shopito.Constants
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.extension.round
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen
import dev.krazymanj.shopito.ui.components.CustomDatePickerDialog
import dev.krazymanj.shopito.ui.components.InfoElement
import dev.krazymanj.shopito.ui.components.SuggestionTextField
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.utils.DateUtils

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

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        val mapLocationResult = navRouter.getValue<String>(Constants.LOCATION)
        mapLocationResult?.value?.let {
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Location> = moshi.adapter(Location::class.java)
            val location = jsonAdapter.fromJson(it)
            navRouter.removeValue<Double>(Constants.LOCATION)
            location?.let { loc ->
                viewModel. onLocationChanged(loc.latitude, loc.longitude)
            }
        }
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
        navigationRouter = navRouter,
        onBackClick = {
            navRouter.returnBack()
        }
    ) {
        AddEditShoppingItemScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel,
            navRouter = navRouter
        )
    }
}

@Composable
fun AddEditShoppingItemScreenContent(
    paddingValues: PaddingValues,
    state: AddEditShoppingItemState,
    actions: AddEditShoppingItemActions,
    navRouter: INavigationRouter
) {

    var showDatePicker by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize().pointerInput(Unit){
            detectTapGestures {
                focusManager.clearFocus()
            }
        }
    ){
        Column(
            modifier = Modifier.padding(paddingValues).padding(spacing16),
            verticalArrangement = Arrangement.spacedBy(spacing8)
        ) {
            SuggestionTextField(
                value = state.shoppingItem.itemName,
                suggestions = if (state.isSaved) emptyList() else state.itemKeywords.map { it.value },
                onValueChange = {
                    actions.onItemNameChange(it)
                },
                label = {
                    Text(stringResource(R.string.name_label))
                },
                errorMsgId = state.nameInputError
            )
            OutlinedTextField(
                value = state.amountInput,
                onValueChange = {
                    actions.onAmountChange(it)
                },
                label = {
                    Text(stringResource(R.string.amount_label))
                },
                isError = state.amountInputError != null,
                supportingText = {
                    state.amountInputError?.let {
                        Text(stringResource(it))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            if (showDatePicker) {
                CustomDatePickerDialog(
                    date = state.shoppingItem.buyTime,
                    onDateSelected = { actions.onBuyTimeChanged(it) },
                    onDismiss = { showDatePicker = false }
                )
            }

            InfoElement(
                value = if (state.shoppingItem.buyTime != null) DateUtils.getDateString(state.shoppingItem.buyTime!!) else null,
                hint = stringResource(R.string.date_label),
                leadingIcon = Lucide.Calendar,
                onClick = {
                    showDatePicker = true
                }, onClearClick = {
                    actions.onBuyTimeChanged(null)
                })

            InfoElement(
                value = if (state.shoppingItem.hasLocation()) "${state.shoppingItem.latitude!!.round()}, ${state.shoppingItem.longitude!!.round()} " else null,
                hint = stringResource(R.string.location_label),
                leadingIcon = Lucide.Pin,
                onClick = {
                    navRouter.navigateTo(Destination.MapLocationPickerScreen(
                        state.shoppingItem.latitude,
                        state.shoppingItem.longitude
                    ))
                }, onClearClick = {
                    actions.onLocationChanged(null, null)
                })


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
}
