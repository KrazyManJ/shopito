package dev.krazymanj.shopito.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.NavGraph
import dev.krazymanj.shopito.ui.theme.ShopitoTheme
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { viewModel.state.value.isLoading }


        enableEdgeToEdge()
        setContent {
            ShopitoTheme {
                Box(
                    modifier = Modifier.fillMaxSize().background(backgroundPrimaryColor())
                ) {
                    NavGraph(
                        startDestination = Destination.ShoppingListsSummaryScreen
                    )
                }
            }
        }
    }
}
