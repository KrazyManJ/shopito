package dev.krazymanj.shopito.ui.elements.modal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.DialogProperties
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor
import dev.krazymanj.shopito.ui.theme.textPrimaryColor

@Composable
fun BaseAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    dismissButton: @Composable (() -> Unit)? = null,
    confirmButton: @Composable (() -> Unit)? = null,
    shape: Shape = AlertDialogDefaults.shape,
    properties: DialogProperties = DialogProperties(),
    content: @Composable (() -> Unit)? = null,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = icon,
        title = title,
        text = content,
        dismissButton = dismissButton,
        confirmButton = confirmButton ?: {},
        containerColor = backgroundSecondaryColor(),
        textContentColor = textPrimaryColor(),
        titleContentColor = textPrimaryColor(),
        modifier = modifier,
        shape = shape,
        properties = properties
    )
}