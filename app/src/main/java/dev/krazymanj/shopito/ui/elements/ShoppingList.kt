package dev.krazymanj.shopito.ui.elements

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.ui.UITestTag
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8

@Composable
fun ShoppingList(
    shoppingList: ShoppingList,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current

    FilledCard(
        modifier = modifier.then(Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                }
            )
            .testTag(UITestTag.ShoppingList.ShoppingList)
        )
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
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag(UITestTag.ShoppingList.ShoppingListName)
                )
                Text(
                    text = shoppingList.description.ifBlank { stringResource(R.string.description_not_provided) },
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = if (shoppingList.description.isBlank()) FontStyle.Italic else null,
                    modifier = Modifier.testTag(UITestTag.ShoppingList.ShoppingListDescription)
                )
            }
        }
    }
}