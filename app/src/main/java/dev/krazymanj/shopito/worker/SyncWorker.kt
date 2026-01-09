package dev.krazymanj.shopito.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.krazymanj.shopito.core.SyncManager

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncManager: SyncManager
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val result = syncManager.sync()
        return when (result) {
            is SyncManager.Result.Error -> {
                val error = workDataOf("ERROR_MSG" to result.message)

                if (result.message == "No internet connection" || result.message == "Unknown error") {
                    Result.retry()
                } else {
                    Result.failure(error)
                }
            }
            SyncManager.Result.Success -> Result.success()
        }
    }
}