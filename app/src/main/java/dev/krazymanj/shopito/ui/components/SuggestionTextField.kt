package dev.krazymanj.shopito.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties

@Composable
fun SuggestionTextField(
    value: String,
    modifier: Modifier = Modifier,
    suggestions: List<String>,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredSuggestions = mutableListOf<String>()
    filteredSuggestions.clear()
    filteredSuggestions.addAll(suggestions.filter { it.contains(value, ignoreCase = true) })

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                expanded = it.isNotBlank()
                if (it.isBlank()) filteredSuggestions.clear()
                onValueChange(it)
            },
            label = label,
            modifier = Modifier.fillMaxWidth()
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
