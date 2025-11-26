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
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Save
import com.composables.icons.lucide.Trash
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
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
    onItemNameChange: (String) -> Unit,
    onCheckStateChange: (Boolean) -> Unit,
    onAmountChange: (Int) -> Unit,
    onDateChange: (Long?) -> Unit,
    onLocationChangeRequest: () -> Unit,
    onLocationClearRequest: () -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    shoppingList: ShoppingList? = null,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = backgroundPrimaryColor(),
        contentColor = textPrimaryColor(),
        modifier = modifier.then(Modifier)
    ) {
        Column(
            modifier = Modifier.padding(spacing16)
        ) {
            shoppingList?.let {
                Row {
                    Icon(imageVector = Lucide.ListTodo, contentDescription = null)
                    Icon(imageVector = Lucide.ChevronRight, contentDescription = null, tint = textSecondaryColor())
                    Text(
                        text = shoppingList.name,
                        color = Primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {

                        }
                    )
                }
            }
            Row {
                BorderFreeTextField(
                    value = shoppingItem.itemName,
                    onValueChange = onItemNameChange,
                    placeholder = "",
                    textStyle = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(1f)
                )
                ShopitoCheckbox(
                    checked = shoppingItem.isDone,
                    onCheckedChange = { onCheckStateChange(!shoppingItem.isDone) },
                    modifier = Modifier.width(40.dp).height(40.dp)
                )
            }
            Spacer(Modifier.height(spacing32))
            DatePickerChip(
                date = shoppingItem.buyTime,
                onDateChange = {
                    onDateChange(it)
                }
            )
            LocationPickerChip(
                location = shoppingItem.location,
                onLocationChangeRequest = { onLocationChangeRequest() },
                onLocationClearRequest = { onLocationClearRequest() }
            )
            Spacer(Modifier.height(spacing32))
            Row {
                OutlinedButton(
                    onClick = { onDelete() },
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
                Button(onClick = { onSave() }) {
                    Icon(imageVector = Lucide.Save, contentDescription = null, Modifier.size(16.dp))
                    Spacer(Modifier.width(spacing8))
                    Text("Save Item")
                }
            }
        }
    }

}