package dev.krazymanj.shopito.worker

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.core.SyncManager
import dev.krazymanj.shopito.core.UserManager
import dev.krazymanj.shopito.core.snackbar.SnackbarManager
import dev.krazymanj.shopito.core.snackbar.SnackbarMessage
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.NavigationManager
import dev.krazymanj.shopito.ui.UiText

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncManager: SyncManager,
    private val userManager: UserManager,
    private val snackbarManager: SnackbarManager,
    private val navigationManager: NavigationManager
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val ERROR_KEY = "ERROR_MSG"
    }

    override suspend fun doWork(): Result {
        val result = syncManager.sync()
        return when (result) {
            is SyncManager.Result.Error -> {
                val error = workDataOf(ERROR_KEY to result.errorId)

                if (result.errorId == R.string.error_no_internet_connection || result.errorId == R.string.error_unknown) {
                    Result.retry()
                } else {
                    Result.failure(error)
                }
            }
            SyncManager.Result.Success -> Result.success()
            SyncManager.Result.Unauthorized -> {
                userManager.logout()
                snackbarManager.showMessage(SnackbarMessage(
                    message = UiText.StringResource(R.string.login_expired),
                    actionLabel = UiText.StringResource(R.string.login_label),
                    onAction = {
                        navigationManager.navigate(Destination.AuthScreen)
                    },
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true,
                    reAppearOnNavigation = true
                ))
                Result.failure(workDataOf(ERROR_KEY to R.string.error_unauthorized))
            }
        }
    }
}