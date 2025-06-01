package dev.krazymanj.shopito.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.ListChecks
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ScrollText
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: Destination,
    val displaySelectedOnRoutes: List<Destination>
)

val navigationItems: List<NavigationItem> = listOf(
    NavigationItem(
        label = "Lists Summary",
        icon = Lucide.ScrollText,
        route = Destination.ShoppingListsSummaryScreen,
        displaySelectedOnRoutes = listOf(
            Destination.ShoppingListsSummaryScreen
        )
    ),
    NavigationItem(
        label = "Shopping Lists",
        icon = Lucide.ListChecks,
        route = Destination.ShoppingListsScreen,
        displaySelectedOnRoutes = listOf(
            Destination.ShoppingListsScreen
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String,
    navigationRouter: INavigationRouter,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(topBarText)
                },
                navigationIcon = {
                    onBackClick?.let {
                        IconButton(onClick = onBackClick) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "return")
                        }
                    }
                },
                actions = actions
            )
        },
        bottomBar = {
            NavigationBar {
                navigationItems.forEach {
                    NavigationBarItem(
                        selected = it.displaySelectedOnRoutes.any { v -> navigationRouter.isCurrentRouteOfClass(v::class) },
                        onClick = {
                            if (!navigationRouter.isCurrentRouteOfClass(it.route::class)) {
                                navigationRouter.navigateTo(it.route)
                            }
                        },
                        label = {
                            Text(text = it.label)
                        },
                        icon = {
                            Image(
                                imageVector = it.icon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = floatingActionButton
    ) {
        content(it)
    }
}
