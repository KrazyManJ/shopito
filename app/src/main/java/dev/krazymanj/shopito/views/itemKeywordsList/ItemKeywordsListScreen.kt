package dev.krazymanj.shopito.views.itemKeywordsList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trash
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.components.BaseScreen

@Composable
fun ItemKeywordsListScreen(
    navRouter: INavigationRouter
) {
    val viewModel = hiltViewModel<ItemKeywordsListViewModel>()

    LaunchedEffect(true) {
        viewModel.loadItemKeywords()
    }

    val state = viewModel.itemKeywordsListUIState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = "Shopping Item Database",
        navigationRouter = navRouter,
        onBackClick = {
            navRouter.returnBack()
        }
    ) {
        ItemKeywordsListScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel
        )
    }
}

@Composable
fun ItemKeywordsListScreenContent(
    paddingValues: PaddingValues,
    state: ItemKeywordsListUIState,
    actions: ItemKeywordsListActions
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        items(state.itemKeywords) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = it.value,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        actions.deleteItemKeyword(it)
                    }
                ) {
                    Icon(
                        imageVector = Lucide.Trash,
                        contentDescription = "Remove"
                    )
                }
            }
        }
    }
}
