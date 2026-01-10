package dev.krazymanj.shopito.core.snackbar

import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.extension.ellipsis
import dev.krazymanj.shopito.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackbarManager @Inject constructor() {
    private val _messageEvent = Channel<SnackbarMessage>(Channel.BUFFERED)

    val messageEvent = _messageEvent.receiveAsFlow()

    suspend fun showMessage(message: SnackbarMessage) {
        _messageEvent.send(message)
    }

    suspend fun showUndo(
        message: UiText,
        onUndo: suspend () -> Unit
    ) {
        showMessage(SnackbarMessage(
            message = message,
            actionLabel = UiText.StringResource(R.string.undo),
            onAction = onUndo,
            withDismissAction = true
        ))
    }

    suspend fun showDeletedItem(
        shoppingItem: ShoppingItem,
        onUndo: suspend (ShoppingItem) -> Unit
    ) {
        showUndo(
            message = UiText.StringResource(R.string.item_deleted, shoppingItem.itemName.ellipsis(15)),
        ) {
            onUndo(shoppingItem)
        }
    }
}