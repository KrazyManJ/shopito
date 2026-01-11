package dev.krazymanj.shopito.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.UserRound
import dev.krazymanj.shopito.model.TokenData
import dev.krazymanj.shopito.ui.elements.modal.LogoutDialog
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing4
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor
import dev.krazymanj.shopito.utils.DateUtils

@Composable
fun AccountCard(
    loggedData: TokenData?,
    isSyncing: Boolean,
    onSyncRequest: () -> Unit,
    onAuthActionRequest: () -> Unit,
    syncError: String?,
    lastTimeSynced: Long?,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismissRequest = { showLogoutDialog = false },
            onConfirm = { wipeData ->
                showLogoutDialog = false
                onAuthActionRequest()
            }
        )
    }

    FilledCard(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(spacing16),
            verticalArrangement = Arrangement.spacedBy(spacing8)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing16)
            ) {
                Icon(
                    imageVector = Lucide.UserRound,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                Column {
                    Text(
                        text = loggedData?.username ?: "Guest",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = when {
                            lastTimeSynced != null -> "Last sync: ${DateUtils.getDateTimeString(lastTimeSynced)}"
                            syncError != null -> syncError
                            isSyncing -> "Syncing..."
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            syncError != null -> Primary
                            else -> textSecondaryColor()
                        }
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = spacing4),
                color = textSecondaryColor()
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loggedData != null) {
                    TextButton(
                        onClick = {
                            onSyncRequest()
                        }
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator()
                        }
                        else {
                            Text("Sync")
                        }
                    }
                }
                Spacer(modifier = Modifier)
                TextButton(
                    onClick = {
                        if (loggedData != null) {
                            showLogoutDialog = true
                        } else {
                            onAuthActionRequest()
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = textPrimaryColor())
                ) {
                    Text(text = if (loggedData != null) "Logout" else "Login")
                }
            }
        }
    }
}