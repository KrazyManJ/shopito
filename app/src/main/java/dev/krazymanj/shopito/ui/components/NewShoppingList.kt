package dev.krazymanj.shopito.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.ui.theme.ShopitoTheme
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textSecondaryColor
import kotlin.io.path.Path

@Composable
fun NewShoppingList(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strokeWidth: Dp = 2.dp
    val cornerRadius: Dp = 16.dp
    val strokePx = with(LocalDensity.current) { strokeWidth.toPx() }
    val cornerPx = with(LocalDensity.current) { cornerRadius.toPx() }
    val dashLengths = floatArrayOf(32f, 32f)
    val color  = textSecondaryColor()

    Card(
        colors = CardColors(
            containerColor = Color.Transparent,
            contentColor = textSecondaryColor(),
            disabledContainerColor = Color.Transparent,
            disabledContentColor = textSecondaryColor()
        ),
        modifier = modifier.then(
            Modifier
                .padding(strokeWidth/2)
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = color,
                        style = Stroke(
                            width = strokePx,
                            pathEffect = PathEffect.dashPathEffect(dashLengths,0f),
                            join = StrokeJoin.Round
                        ),
                        cornerRadius = CornerRadius(cornerPx)
                    )
                }
                .clickable { onClick() }
                .padding(spacing16)
        ),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Lucide.Plus,
                contentDescription = stringResource(R.string.add),
                modifier = Modifier.size(48.dp),
                tint = textSecondaryColor()
            )
            Text(
                text = stringResource(R.string.add_shopping_list_title),
                color = textSecondaryColor()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ShopitoTheme {
        NewShoppingList({})
    }
}