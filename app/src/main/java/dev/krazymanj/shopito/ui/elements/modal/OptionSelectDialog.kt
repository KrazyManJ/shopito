package dev.krazymanj.shopito.ui.elements.modal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.ui.theme.Emphasized
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@Composable
fun <T> OptionSelectDialog(
    options: List<T>,
    row: @Composable (T) -> Unit,
    onOptionSelect: (T) -> Unit,
    title: String,
    onDismissRequest: () -> Unit,
    enabledOptionsPredicate: (T) -> Boolean = { true }
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundPrimaryColor(),
                contentColor = textPrimaryColor()
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(spacing16)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = spacing16, vertical = spacing8),
                    fontWeight = FontWeight.Emphasized
                )
                Spacer(Modifier.height(spacing16))

                LazyColumn {
                    if (options.isEmpty()) {
                        item {
                            Text(
                                text = "No available options...",
                                color = textSecondaryColor(),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(spacing16)
                            )
                        }
                    }
                    items(items = options) {
                        val enabled = enabledOptionsPredicate(it)
                        Column(
                            modifier = Modifier.fillMaxWidth().then(
                                if (enabled)
                                    Modifier.clickable { onOptionSelect(it) }
                                else
                                    Modifier
                            )
                        ) {
                            CompositionLocalProvider(
                                value = LocalContentColor provides if (enabledOptionsPredicate(it)) textPrimaryColor() else textSecondaryColor()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = spacing8, vertical = spacing16)
                                ) {
                                    row(it)
                                }
                                if (it != options.last()) {
                                    HorizontalDivider(
                                        color = textSecondaryColor(),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(spacing32))
                Row {
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text(text = stringResource(R.string.cancel_label))
                    }
                }
            }
        }
    }
}