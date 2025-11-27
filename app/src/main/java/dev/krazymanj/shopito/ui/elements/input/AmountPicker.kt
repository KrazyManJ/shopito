package dev.krazymanj.shopito.ui.elements.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.ui.elements.button.RepeatingOutlinedIconButton
import dev.krazymanj.shopito.ui.theme.Emphasized

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AmountPicker(
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {


    Row(
        modifier = modifier.then(Modifier.fillMaxWidth()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RepeatingOutlinedIconButton(
            icon = Lucide.ChevronLeft,
            onClick = onDecrement,
            enabled = value > 1,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(
                50,
                25,
                25,
                50
            )
        )
        Text(
            text = "${value}x",
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Emphasized
        )
        RepeatingOutlinedIconButton(
            icon = Lucide.ChevronRight,
            onClick = onIncrement,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(
                25,
                50,
                50,
                25
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AmountPicker(1, {}, {})
}