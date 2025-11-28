package dev.krazymanj.shopito.ui.elements.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor
import kotlinx.coroutines.delay

@Composable
fun RepeatingOutlinedIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.outlinedShape,
    maxDelayMillis: Long = 300,
    minDelayMillis: Long = 30,
    delayDecayFactor: Float = .15f,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val isPressed by interactionSource.collectIsPressedAsState()

    val currentOnClick by rememberUpdatedState(onClick)

    LaunchedEffect(isPressed, enabled) {
        var currentDelayMillis = maxDelayMillis

        while (enabled && isPressed) {
            currentOnClick()
            delay(currentDelayMillis)
            currentDelayMillis =
                (currentDelayMillis - (currentDelayMillis * delayDecayFactor))
                    .toLong().coerceAtLeast(minDelayMillis)
        }
    }

    OutlinedIconButton(
        enabled = enabled,
        shape = shape,
        onClick = {},
        modifier = modifier,
        interactionSource = interactionSource,
        colors = IconButtonDefaults.outlinedIconButtonColors(
            contentColor = textPrimaryColor(),
            disabledContentColor = textSecondaryColor(),
        ),
        border = IconButtonDefaults.outlinedIconButtonBorder(true).copy(
            brush = SolidColor(if (enabled) textPrimaryColor() else textSecondaryColor())
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}