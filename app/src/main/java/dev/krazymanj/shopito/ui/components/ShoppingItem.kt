package dev.krazymanj.shopito.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.Trash
import dev.krazymanj.shopito.database.entities.ShoppingItem

@Composable
fun ShoppingItem(
    shoppingItem: ShoppingItem,
    modifier: Modifier = Modifier,
    onEditButtonClick: (() -> Unit)? = null,
    onDeleteButtonClick: (() -> Unit)? = null,
    onCheckStateChange: ((Boolean) -> Unit)? = null,
){
    var opened by remember { mutableStateOf(false) }

    var topModifier = modifier.then(Modifier)

    if (opened) topModifier = topModifier.shadow(8.dp)

    Column {
        Box(
            modifier = topModifier.clickable {
                opened = !opened
            }
        ) {
            Text(
                text = shoppingItem.itemName,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(shoppingItem.amount.toString()+"x")
                Checkbox(
                    checked = shoppingItem.isDone,
                    onCheckedChange = {
                        if (onCheckStateChange != null) onCheckStateChange(it)
                    }
                )
            }
        }
        if (opened) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        IconButton(
                            onClick = {
                                if (onEditButtonClick != null) onEditButtonClick()
                            }
                        ) {
                            Icon(imageVector = Lucide.Pencil, contentDescription = null)
                        }
                        IconButton(
                            onClick = {
                                if (onDeleteButtonClick != null) {
                                    opened = false
                                    onDeleteButtonClick()
                                }
                            }
                        ) {
                            Icon(imageVector = Lucide.Trash, contentDescription = null, tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}