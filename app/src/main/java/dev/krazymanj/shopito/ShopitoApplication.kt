package dev.krazymanj.shopito

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import dev.krazymanj.shopito.worker.WorkScheduler
import javax.inject.Inject

@HiltAndroidApp
class ShopitoApplication : Application(), Configuration.Provider, DefaultLifecycleObserver {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    @Inject
    lateinit var workScheduler: WorkScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        workScheduler.scheduleOneTimeSync()
    }
}