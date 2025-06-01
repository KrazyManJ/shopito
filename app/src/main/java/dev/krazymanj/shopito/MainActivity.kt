package dev.krazymanj.shopito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.NavGraph
import dev.krazymanj.shopito.ui.theme.ShopitoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopitoTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavGraph(
                        startDestination = Destination.ShoppingListsSummaryScreen
                    )
                }
            }
        }
    }
}
