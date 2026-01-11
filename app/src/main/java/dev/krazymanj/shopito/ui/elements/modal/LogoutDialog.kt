package dev.krazymanj.shopito.ui.elements.modal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.ui.elements.input.ShopitoCheckbox
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing4
import dev.krazymanj.shopito.ui.theme.textPrimaryColor

@Composable
fun LogoutDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (wipeData: Boolean) -> Unit
) {
    var checkBoxState by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(R.string.logout_label))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing16)
            ) {
                Text(stringResource(R.string.logout_modal_text))
                Text(stringResource(R.string.logout_modal_wipe_data_text))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing4)
                ) {
                    ShopitoCheckbox(checked = checkBoxState, onCheckedChange = { checkBoxState = it })
                    Text(stringResource(R.string.wipe_data_label))
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = textPrimaryColor(),
                )
            ) {
                Text(stringResource(R.string.dismiss_label))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(checkBoxState)
                }
            ) {
                Text(
                    text = if (checkBoxState)
                        stringResource(R.string.logout_and_wipe_data_label)
                    else
                        stringResource(R.string.logout_label)
                )
            }
        },
        containerColor = backgroundSecondaryColor(),
        textContentColor = textPrimaryColor(),
        titleContentColor = textPrimaryColor()
    )
}