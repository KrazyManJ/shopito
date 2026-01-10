package dev.krazymanj.shopito.ui.elements.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.ui.elements.modal.OptionSelectDialog

@Composable
fun <T> SuggestionField(
    options: List<T>,
    labelProvider: @Composable (T) -> String,
    value: T?,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    enabled: Boolean = true
) {
    var optionsVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    if (optionsVisible) {
        OptionSelectDialog(
            options = options,
            row = {
                Text(labelProvider(it))
            },
            title = "Select an option",
            onDismissRequest = {
                optionsVisible = false
                focusManager.clearFocus(true)
            },
            onOptionSelect = {
                onValueChange(it)
                optionsVisible = false
                focusManager.clearFocus(true)
            }
        )
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    if (isPressed) {
        LaunchedEffect(Unit) {
            optionsVisible = true
        }
    }

    OutlinedTextField(
        value = if (value != null) labelProvider(value) else "-- Select a value --",
        onValueChange = {},
        trailingIcon = {
            Icon(
                imageVector = Lucide.ChevronDown,
                contentDescription = null
            )
        },
        readOnly = true,
        modifier = modifier,
        interactionSource = interactionSource,
        label = if (labelText != null) ({
            Text(
                text = labelText
            )
        }) else null,
        enabled = enabled
    )
}