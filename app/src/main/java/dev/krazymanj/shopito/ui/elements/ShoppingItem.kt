package dev.krazymanj.shopito.ui.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trash
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.ui.UITestTag
import dev.krazymanj.shopito.ui.elements.input.ShopitoCheckbox
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing4
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItem(
    shoppingItem: ShoppingItem,
    modifier: Modifier = Modifier,
    onCheckStateChange: (Boolean) -> Unit,
    shoppingList: ShoppingList? = null,
    onClick: () -> Unit = {},
    onRemove: () -> Unit,
) {
    val textColor = if (shoppingItem.isDone) textSecondaryColor() else textPrimaryColor()
    val textDecoration = if (shoppingItem.isDone) TextDecoration.LineThrough else TextDecoration.None

    val alpha by animateFloatAsState(if (shoppingItem.isDone) 0.5f else 1f)

    var isRemoved by remember { mutableStateOf(false) }

    val fadeTime = 300

    LaunchedEffect(isRemoved, shoppingItem) {
        if (isRemoved) {
            delay(fadeTime.toLong())
            onRemove()
        }
    }

    val density = LocalDensity.current
    val dismissState = remember(key1 = shoppingItem){
        SwipeToDismissBoxState(
            SwipeToDismissBoxValue.Settled
        ) { with(density) { 56.dp.toPx() } }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = fadeOut(animationSpec = tween(durationMillis = fadeTime)) +
                shrinkVertically(animationSpec = tween(durationMillis = fadeTime))
    ) {
        SwipeToDismissBox(
            state = dismissState,
            onDismiss = { dismissValue ->
                when (dismissValue) {
                    SwipeToDismissBoxValue.EndToStart, SwipeToDismissBoxValue.StartToEnd -> {
                        isRemoved = true
                        true
                    }
                    else -> {
                        false
                    }
                }
            },
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Primary)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Lucide.Trash,
                        contentDescription = stringResource(R.string.remove),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        ) {
            Row(
                modifier = modifier.then(Modifier
                    .background(backgroundPrimaryColor())
                    .clickable { onClick() }
                    .alpha(alpha)
                    .padding(
                        horizontal = spacing16,
                        vertical = spacing4
                    )
                    .testTag(UITestTag.ShoppingItem.ShoppingItem)
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
                        maxLines = 1,
                        modifier = Modifier.testTag(UITestTag.ShoppingItem.Name)
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
                    textDecoration = textDecoration,
                    modifier = Modifier.testTag(UITestTag.ShoppingItem.Amount)
                )
                ShopitoCheckbox(
                    checked = shoppingItem.isDone,
                    onCheckedChange = onCheckStateChange,
                    modifier = Modifier.padding(start = spacing16)
                )
            }
        }
    }
}