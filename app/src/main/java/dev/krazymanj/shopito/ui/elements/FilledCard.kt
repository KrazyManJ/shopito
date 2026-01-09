package dev.krazymanj.shopito.ui.elements

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor

@Composable
fun FilledCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundSecondaryColor()
        ),
        modifier = modifier,
        content = content
    )
}