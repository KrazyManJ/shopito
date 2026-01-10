package dev.krazymanj.shopito.ui.elements.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.core.snackbar.rememberGlobalSnackbarHostState
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String,
    modifier: Modifier = Modifier,
    topBarTextDescription: String? = null,
    onBackClick: (() -> Unit)? = null,
    showLoading: Boolean = false,
    placeholderScreenContent: PlaceholderScreenContent? = null,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    val snackbarHostState = rememberGlobalSnackbarHostState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = topBarText,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = spacing16),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        topBarTextDescription?.let {
                            Text(
                                text = it,
                                color = textSecondaryColor(),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
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
                    actionIconContentColor = textPrimaryColor(),
                    subtitleContentColor = textSecondaryColor()
                )
            )
        },
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        containerColor = backgroundPrimaryColor(),
        contentColor = textPrimaryColor(),
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    actionColor = Primary
                )
            }
        },
        modifier = modifier.then(Modifier.imePadding())
    ) {
        if (showLoading) {
            LoadingScreen()
        }
        else if (placeholderScreenContent != null) {
            PlaceHolderScreen(
                content = placeholderScreenContent
            )
        }
        else {
            content(it)
        }
    }
}
