package dev.krazymanj.shopito.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.PopupProperties

@SuppressLint("RememberReturnType")
@Composable
fun SuggestionTextField(
    value: String,
    modifier: Modifier = Modifier,
    suggestions: List<String>,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit = {},
    errorMsgId: Int? = null
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredSuggestions = mutableListOf<String>()
    filteredSuggestions.clear()
    filteredSuggestions.addAll(suggestions.filter { it.contains(value, ignoreCase = true) })

    val interactionSource = remember { MutableInteractionSource() }

    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        expanded = if (isFocused) value.isNotBlank() && suggestions.isNotEmpty() else false
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                expanded = suggestions.isNotEmpty()
                if (it.isBlank()) filteredSuggestions.clear()
                onValueChange(it)
            },
            label = label,
            modifier = Modifier.fillMaxWidth(),
            isError = errorMsgId != null,
            supportingText = {
                errorMsgId?.let {
                    Text(stringResource(it))
                }
            },
            interactionSource = interactionSource
        )
        if (filteredSuggestions.isNotEmpty() && value.isNotBlank()){
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {},
                modifier = Modifier
                    .fillMaxWidth(),
                properties = PopupProperties(focusable = false)
            ) {
                filteredSuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion) },
                        onClick = {
                            onValueChange(suggestion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
