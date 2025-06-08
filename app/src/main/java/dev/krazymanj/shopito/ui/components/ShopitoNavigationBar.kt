package dev.krazymanj.shopito.ui.components

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.ListChecks
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ScrollText
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.navigation.NavigationRouterImpl
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import kotlin.reflect.KClass

data class NavigationItem(
    val label: Int,
    val icon: ImageVector,
    val route: Destination,
    val displaySelectedOnRoutes: List<KClass<out Destination>>
)

val navigationItems: List<NavigationItem> = listOf(
    NavigationItem(
        label = R.string.navigation_lists_summary_label,
        icon = Lucide.ScrollText,
        route = Destination.ShoppingListsSummaryScreen,
        displaySelectedOnRoutes = listOf(
            Destination.ShoppingListsSummaryScreen::class
        )
    ),
    NavigationItem(
        label = R.string.navigation_shopping_lists_label,
        icon = Lucide.ListChecks,
        route = Destination.ShoppingListsScreen,
        displaySelectedOnRoutes = listOf(
            Destination.ShoppingListsScreen::class,
            Destination.ViewShoppingList::class
        )
    )
)

@Composable
fun ShopitoNavigationBar(
    navigationRouter: INavigationRouter,
) {
    NavigationBar(
        containerColor = Color.Transparent
    ) {
        navigationItems.forEach {
            NavigationBarItem(
                selected = it.displaySelectedOnRoutes.any { v -> navigationRouter.isCurrentRouteOfClass(v) },
                onClick = {
                    if (!navigationRouter.isCurrentRouteOfClass(it.route::class)) {
                        navigationRouter.navigateTo(it.route)
                    }
                },
                label = {
                    Text(text = stringResource(it.label))
                },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = stringResource(it.label)
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = textPrimaryColor(),
                    unselectedTextColor = textPrimaryColor(),
                    disabledIconColor = textPrimaryColor(),
                    disabledTextColor = textPrimaryColor()
                ),
                modifier = Modifier.background(Color.Transparent)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val navRouter = NavigationRouterImpl(
        navController = rememberNavController()
    )
    ShopitoNavigationBar(
        navigationRouter = navRouter
    )
}