package dev.krazymanj.shopito.ui.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing4

@Composable
fun ShopitoCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) Primary else Color.Transparent
    )
    Box(
        modifier = modifier.then(Modifier
            .padding(spacing4)
            .border(2.dp, Primary, RoundedCornerShape(6.dp))
            .height(24.dp)
            .width(24.dp)
            .drawBehind {
                val cornerRadius = 6.dp.toPx()
                drawRoundRect(
                    color = backgroundColor,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                onCheckedChange(!checked)
            }
        )
    ) {
        AnimatedVisibility(
            checked,
            modifier = Modifier.align(Alignment.Center),
            enter = scaleIn(initialScale = 0.5f),
            exit = scaleOut()
        ) {
            Icon(
                imageVector = Lucide.Check,
                contentDescription = null,
                tint = backgroundPrimaryColor() // Assuming this is your text color
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Row {
        ShopitoCheckbox(false, {})
        ShopitoCheckbox(true, {})
    }
}