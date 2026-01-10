package dev.krazymanj.shopito.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.krazymanj.shopito.core.UserManager
import dev.krazymanj.shopito.navigation.NavGraph
import dev.krazymanj.shopito.ui.theme.ShopitoTheme
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.worker.WorkScheduler
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    @Inject
    lateinit var workScheduler: WorkScheduler

    @Inject
    lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { viewModel.state.value.isLoading }

        checkAndSync()

        enableEdgeToEdge()
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()

            if (!state.isLoading) {
                ShopitoTheme {
                    Box(
                        modifier = Modifier.fillMaxSize().background(backgroundPrimaryColor())
                    ) {
                        NavGraph(
                            startDestination = state.resolveStartDestination()
                        )
                    }
                }
            }
        }
    }

    private fun checkAndSync() {
        lifecycleScope.launch {
            if (userManager.isLoggedIn()) {
                workScheduler.scheduleOneTimeSync()

                workScheduler.schedulePeriodicSync()
            }
        }
    }
}
