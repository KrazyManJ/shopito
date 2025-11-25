package dev.krazymanj.shopito.ui.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.textPrimaryColor



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String,
    navigationRouter: INavigationRouter,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    showBottomNavigationBar: Boolean = false,
    customBottomBar: @Composable (() -> Unit)? = null,
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = topBarText,
                        fontWeight = FontWeight.W500,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        modifier = Modifier.padding(horizontal = spacing16),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    onBackClick?.let {
                        IconButton(onClick = onBackClick) {
                            Icon(imageVector = Lucide.ArrowLeft, contentDescription = stringResource(R.string.return_back))
                        }
                    }
                },
                actions = actions,
                colors = TopAppBarColors(
                    containerColor = backgroundPrimaryColor(),
                    scrolledContainerColor = backgroundPrimaryColor(),
                    navigationIconContentColor = textPrimaryColor(),
                    titleContentColor = textPrimaryColor(),
                    actionIconContentColor = textPrimaryColor()
                )
            )
        },
        bottomBar = {
            if (showBottomNavigationBar) {
                ShopitoNavigationBar(
                    navigationRouter = navigationRouter
                )
            }
            customBottomBar?.let { it() }
        },
        floatingActionButton = floatingActionButton,
        containerColor = backgroundPrimaryColor(),
        contentColor = textPrimaryColor(),
        modifier = Modifier.imePadding()
    ) {
        content(it)
    }
}
