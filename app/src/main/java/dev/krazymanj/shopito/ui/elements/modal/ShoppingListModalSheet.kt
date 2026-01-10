package dev.krazymanj.shopito.ui.elements.modal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trash
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListModalSheet(
    shoppingListId: String?,
    onDismissRequest: () -> Unit,
    onAfterSave: () -> Unit = {},
    onAfterRemove: () -> Unit = {},
) {
    val viewModel: ShoppingListModalSheetViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadShoppingList(shoppingListId)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.reset()
            onDismissRequest()
        },
        containerColor = backgroundPrimaryColor(),
        contentColor = textPrimaryColor(),
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(spacing16),
            verticalArrangement = Arrangement.spacedBy(spacing8)
        ) {
            Text(
                text = stringResource(when {
                    state.isEdit -> R.string.edit_shopping_list_title
                    else -> R.string.add_shopping_list_title
                })
            )
            OutlinedTextField(
                value = state.shoppingList.name,
                label = { Text(stringResource(R.string.name_label)) },
                onValueChange = { viewModel.onListUpdate(state.shoppingList.copy(name = it)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.shoppingList.description,
                label = { Text(stringResource(R.string.description_label)) },
                onValueChange = { viewModel.onListUpdate(state.shoppingList.copy(description = it)) },
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                if (state.isEdit) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                viewModel.delete()
                                viewModel.reset()
                                onAfterRemove()
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = backgroundPrimaryColor(),
                            contentColor = Primary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder(true).copy(
                            brush = SolidColor(Primary)
                        )
                    ) {
                        Icon(imageVector = Lucide.Trash, contentDescription = null, Modifier.size(16.dp))
                        Spacer(Modifier.width(spacing8))
                        Text(stringResource(R.string.remove))
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            viewModel.save()
                            viewModel.reset()
                            onAfterSave()
                        }
                    },
                    enabled = state.isFormValid()
                ) { Text(text = stringResource(R.string.save_label)) }
            }
        }
    }
}