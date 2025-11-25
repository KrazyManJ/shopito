package dev.krazymanj.shopito.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.Trash
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor
import dev.krazymanj.shopito.utils.DateUtils

@Composable
fun ShoppingItem(
    shoppingItem: ShoppingItem,
    modifier: Modifier = Modifier,
    shoppingList: ShoppingList? = null,
    onEditButtonClick: (() -> Unit)? = null,
    onDeleteButtonClick: (() -> Unit)? = null,
    onCheckStateChange: ((Boolean) -> Unit)? = null,
    forceOpen: Boolean = false
) {
    var opened by remember { mutableStateOf(forceOpen) }

    val shape = RoundedCornerShape(topStart = spacing16, topEnd = spacing16)

    Column {
        Box(
            modifier = modifier.then(Modifier
                .shadow(if (opened) 8.dp else 0.dp, shape)
                .clip(shape)
                .background(backgroundPrimaryColor())
                .clickable { opened = !opened }
                .padding(horizontal = spacing8)
            )
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                shoppingList?.let {
                    Text(
                        text = "> " + shoppingList.name,
                        color = textSecondaryColor(),
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Text(
                    text = shoppingItem.itemName,
                    textDecoration = if (shoppingItem.isDone)
                        TextDecoration.LineThrough
                    else
                        TextDecoration.None,
                    color = if (shoppingItem.isDone)
                        textSecondaryColor()
                    else
                        textPrimaryColor()
                )
            }

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing8)
            ) {
                Text(
                    text = shoppingItem.amount.toString() + "x",
                    textDecoration = if (shoppingItem.isDone)
                        TextDecoration.LineThrough
                    else
                        TextDecoration.None,
                    color = if (shoppingItem.isDone)
                        textSecondaryColor()
                    else
                        textPrimaryColor()
                )
                Checkbox(
                    checked = shoppingItem.isDone,
                    onCheckedChange = {
                        if (onCheckStateChange != null) onCheckStateChange(it)
                    },
                    colors = CheckboxColors(
                        checkedCheckmarkColor = Color.White,
                        uncheckedCheckmarkColor = Color.White,
                        checkedBoxColor = Primary,
                        uncheckedBoxColor = Color.Transparent,
                        disabledCheckedBoxColor = Primary,
                        disabledUncheckedBoxColor = Primary,
                        disabledIndeterminateBoxColor = Primary,
                        checkedBorderColor = Primary,
                        uncheckedBorderColor = Primary,
                        disabledBorderColor = Primary,
                        disabledUncheckedBorderColor = Primary,
                        disabledIndeterminateBorderColor = Primary,
                    )
                )
            }
        }
        if (!opened) return

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = spacing16, bottomEnd = spacing16))
                .background(backgroundSecondaryColor())
                .padding(spacing8)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing8)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.buy_time_label),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        Text(
                            text =
                            if (shoppingItem.buyTime != null)
                                DateUtils.getDateString(shoppingItem.buyTime!!)
                            else
                                stringResource(R.string.unspecified_label)
                        )
                        shoppingItem.buyTime?.let {
                            PrettyTimeText(it) { time -> " (${time})" }
                        }
                    }
                }
                Column {
                    Text(
                        text = stringResource(R.string.location_label),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    LocationAddressText(shoppingItem.latitude, shoppingItem.longitude)
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth()
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
                        Icon(
                            imageVector = Lucide.Trash,
                            contentDescription = null,
                            tint = Primary
                        )
                    }
                }
            }
        }
    }
}