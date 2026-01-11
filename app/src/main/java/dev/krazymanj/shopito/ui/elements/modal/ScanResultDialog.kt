package dev.krazymanj.shopito.ui.elements.modal

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing4
import dev.krazymanj.shopito.ui.theme.spacing64
import dev.krazymanj.shopito.ui.theme.textPrimaryColor

@Composable
private fun LoadingSection() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(spacing64)
    ) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ScanErrorSection(
    @StringRes scanError: Int,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = spacing16)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing16),
        ) {
            Icon(
                imageVector = Lucide.CircleAlert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = stringResource(scanError),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun ScanResultSection(
    shoppingList: ShoppingList,
    scannedItems: List<ShoppingItem>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing16)
    ) {
        Text(
            text = stringResource(R.string.scan_result_modal_text, shoppingList.name),
            style = MaterialTheme.typography.bodySmall,
        )
        LazyColumn(
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            items(items = scannedItems) {
                Row(
                    modifier = Modifier.padding(vertical = spacing4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.itemName,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "${it.amount}x",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
fun ScanResultDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    isScanning: Boolean,
    @StringRes scanError: Int?,
    shoppingList: ShoppingList,
    scannedItems: List<ShoppingItem>,
) {
    BaseAlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            if (!isScanning) {
                Text(text = stringResource(R.string.scan_result_title))
            }
        },
        dismissButton = {
            if (!isScanning) {
                TextButton(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.textButtonColors(contentColor = textPrimaryColor())
                ) {
                    Text(stringResource(R.string.try_again_label))
                }
            }
        },
        confirmButton = {
            if (!isScanning && scanError == null) {
                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.add_items_label))
                }
            }
        }
    ) {
        if (isScanning) {
            LoadingSection()
        }
        else if (scanError != null) {
            ScanErrorSection(scanError)
        }
        else {
            ScanResultSection(
                shoppingList = shoppingList,
                scannedItems = scannedItems
            )
        }
    }
}
