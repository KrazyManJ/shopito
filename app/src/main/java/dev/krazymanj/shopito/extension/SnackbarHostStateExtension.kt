package dev.krazymanj.shopito.extension

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.entities.ShoppingItem

suspend fun SnackbarHostState.showDeletedMessage(
    item: ShoppingItem,
    context: Context,
    onUndo: () -> Unit
) {

    val result = showSnackbar(
        message = context.getString(R.string.item_deleted, item.itemName.ellipsis(15)),
        actionLabel = context.getString(R.string.undo),
        duration = SnackbarDuration.Short,
        withDismissAction = true
    )

    if (result == SnackbarResult.ActionPerformed) {
        onUndo()
    }
}