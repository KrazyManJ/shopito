package dev.krazymanj.shopito.ui.components


import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import dev.krazymanj.shopito.R

@Composable
fun InfoElement(
    value: String?,
    hint: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    onClearClick: () -> Unit){

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    val focusManager = LocalFocusManager.current

    if (isPressed) {
        LaunchedEffect(isPressed){
            onClick()
        }
    }

    OutlinedTextField(
        value = value ?: "",
        onValueChange = {},
        interactionSource = interactionSource,
        leadingIcon = {Icon(
            imageVector = leadingIcon,
            tint = Color.Black,
            contentDescription = null
        )}
        ,
        trailingIcon = if (value != null) {
            {
                IconButton(
                    onClick = {
                        onClearClick()
                        focusManager.clearFocus()
                    }) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Filled.Clear),
                        tint = Color.Black,
                        contentDescription = stringResource(R.string.clear_label)
                    )
                }
            }

        } else {
            null
        },
        label = {Text(text = hint)},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
    )
}