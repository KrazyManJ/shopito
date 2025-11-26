package dev.krazymanj.shopito.ui.elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing4
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItem(
    shoppingItem: ShoppingItem,
    modifier: Modifier = Modifier,
    onCheckStateChange: (Boolean) -> Unit,
    shoppingList: ShoppingList? = null,
    onClick: () -> Unit = {}
) {
    val textColor = if (shoppingItem.isDone) textSecondaryColor() else textPrimaryColor()
    val textDecoration = if (shoppingItem.isDone) TextDecoration.LineThrough else TextDecoration.None

    val alpha by animateFloatAsState(if (shoppingItem.isDone) 0.5f else 1f)

    Row(
        modifier = modifier.then(Modifier
            .clickable { onClick() }
            .alpha(alpha)
            .padding(
                horizontal = spacing16,
                vertical = spacing4
            )
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = shoppingItem.itemName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = textColor,
                textDecoration = textDecoration,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            shoppingList?.let { shoppingList ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing4)
                ) {
                    Icon(
                        imageVector = Lucide.ListTodo,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = textSecondaryColor()
                    )
                    Text(shoppingList.name, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Text(
            text = "${shoppingItem.amount}x",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textDecoration = textDecoration
        )
        ShopitoCheckbox(
            checked = shoppingItem.isDone,
            onCheckedChange = onCheckStateChange,
            modifier = Modifier.padding(start = spacing16)
        )
    }
}