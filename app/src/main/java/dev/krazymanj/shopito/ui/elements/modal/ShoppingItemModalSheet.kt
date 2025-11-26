package dev.krazymanj.shopito.ui.elements.modal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Save
import com.composables.icons.lucide.Trash
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.navigation.NavStateKey
import dev.krazymanj.shopito.navigation.NavigationCurrentStateReceivedEffect
import dev.krazymanj.shopito.ui.elements.BorderFreeTextField
import dev.krazymanj.shopito.ui.elements.ShopitoCheckbox
import dev.krazymanj.shopito.ui.elements.chip.DatePickerChip
import dev.krazymanj.shopito.ui.elements.chip.LocationPickerChip
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItemModalSheet(
    shoppingItem: ShoppingItem,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    shoppingList: ShoppingList? = null,
    onShoppingListLinkClick: () -> Unit = {},
    navRouter: INavigationRouter,
) {
    val viewModel = hiltViewModel<ShoppingItemModalSheetViewModel>()

    val state = viewModel.state.collectAsStateWithLifecycle()

    NavigationCurrentStateReceivedEffect(navRouter, NavStateKey.LocationModalResult) { location ->
        viewModel.updateItem(state.value.item.copy(location = location))
    }

    if (state.value.dismissed) {
        onDismiss()
        viewModel.reset()
    }

    if (state.value.loading) {
        viewModel.loadData(shoppingItem, shoppingList)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = backgroundPrimaryColor(),
        contentColor = textPrimaryColor(),
        modifier = modifier.then(Modifier)
    ) {
        Column(
            modifier = Modifier.padding(spacing16)
        ) {
            state.value.list?.let { shoppingList ->
                Row {
                    Icon(imageVector = Lucide.ListTodo, contentDescription = null)
                    Icon(imageVector = Lucide.ChevronRight, contentDescription = null, tint = textSecondaryColor())
                    Text(
                        text = shoppingList.name,
                        color = Primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            onShoppingListLinkClick()
                        }
                    )
                }
            }
            Spacer(Modifier.height(spacing16))
            Row {
                BorderFreeTextField(
                    value = state.value.item.itemName,
                    onValueChange = { viewModel.updateItem(state.value.item.copy(itemName = it))},
                    placeholder = "",
                    textStyle = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(spacing16))
                ShopitoCheckbox(
                    checked = state.value.item.isDone,
                    onCheckedChange = { viewModel.updateItem(state.value.item.copy(isDone = it))},
                    modifier = Modifier.width(40.dp).height(40.dp)
                )
            }
            Spacer(Modifier.height(spacing32))
            DatePickerChip(
                date = state.value.item.buyTime,
                onDateChange = {
                    viewModel.updateItem(state.value.item.copy(buyTime = it))
                }
            )
            LocationPickerChip(
                location = state.value.item.location,
                onLocationChangeRequest = {
                    navRouter.navigateTo(Destination.MapLocationPickerScreen(
                        state.value.item.location,
                        NavStateKey.LocationModalResult
                    ))
                },
                onLocationClearRequest = {
                    viewModel.updateItem(state.value.item.copy(location = null))
                }
            )
            Spacer(Modifier.height(spacing32))
            Row {
                OutlinedButton(
                    onClick = { viewModel.delete() },
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = backgroundPrimaryColor(),
                        contentColor = Primary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(Primary)
                    )
                ) {
                    Icon(imageVector = Lucide.Trash, contentDescription = null, Modifier.size(16.dp))
                    Spacer(Modifier.width(spacing8))
                    Text("Delete")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { viewModel.save() }) {
                    Icon(imageVector = Lucide.Save, contentDescription = null, Modifier.size(16.dp))
                    Spacer(Modifier.width(spacing8))
                    Text("Save Item")
                }
            }
        }
    }

}