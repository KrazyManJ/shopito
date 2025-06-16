package dev.krazymanj.shopito.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor

@Composable
fun ShoppingList(
    shoppingList: ShoppingList,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardColors(
            containerColor = backgroundSecondaryColor(),
            contentColor = textPrimaryColor(),
            disabledContainerColor = backgroundSecondaryColor(),
            disabledContentColor = textPrimaryColor()
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing16),
            modifier = Modifier.padding(spacing16)
        ) {
            Icon(
                imageVector = Lucide.ListTodo,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing8)
            ) {
                Text(
                    text = shoppingList.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = shoppingList.description.ifBlank { stringResource(R.string.description_not_provided) },
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = if (shoppingList.description.isBlank()) FontStyle.Italic else null
                )
            }
        }
    }
}