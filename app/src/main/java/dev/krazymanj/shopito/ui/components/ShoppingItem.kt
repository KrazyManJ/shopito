package dev.krazymanj.shopito.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import dev.krazymanj.shopito.database.entities.ShoppingItem

@Composable
fun ShoppingItem(
    shoppingItem: ShoppingItem,
    modifier: Modifier = Modifier
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
                Text(shoppingItem.amount.toString())
                Checkbox(
                    checked = shoppingItem.isDone,
                    onCheckedChange = {

                    }
                )
            }
        }
        if (opened) {
            Card {
                Text("This will be openable description")
            }
        }
    }
}