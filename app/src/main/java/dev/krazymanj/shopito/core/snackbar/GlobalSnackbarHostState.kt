package dev.krazymanj.shopito.core.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CancellationException

@Composable
fun rememberGlobalSnackbarHostState(): SnackbarHostState {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: GlobalSnackbarHostStateViewModel = hiltViewModel()

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.snackbarMessages.collect { message ->
                try {
                    val result = snackbarHostState.showSnackbar(
                        message = message.message.asString(context),
                        actionLabel = message.actionLabel?.asString(context),
                        duration = message.duration ?: if (message.actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
                        withDismissAction = message.withDismissAction
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        message.onAction?.invoke()
                    }
                } catch (e: CancellationException) {
                    if (message.reAppearOnNavigation) {
                        viewModel.queueMessageOnNewScreen(message)
                    }
                    throw e
                }
            }
        }
    }

    return snackbarHostState
}