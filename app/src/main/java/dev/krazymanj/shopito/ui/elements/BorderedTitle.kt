package dev.krazymanj.shopito.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.krazymanj.shopito.ui.theme.Emphasized
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor
import dev.krazymanj.shopito.ui.theme.spacing4
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@Composable
fun BorderedTitle(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingText: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth().padding(horizontal = spacing8)
        ) {
            ProvideTextStyle(
                value = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Emphasized)
            ) {
                title()
            }
            Spacer(modifier = Modifier.weight(1f))
            supportingText?.let {
                ProvideTextStyle(
                    value = MaterialTheme.typography.bodyMedium.copy(color = textSecondaryColor())
                ) {
                    supportingText()
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = spacing4),
            color = backgroundSecondaryColor()
        )
    }
}