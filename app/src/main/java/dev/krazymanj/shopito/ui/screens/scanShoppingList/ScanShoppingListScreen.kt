package dev.krazymanj.shopito.ui.screens.scanShoppingList

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Camera
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.core.analyzers.TextRecognizer
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.CameraComposeView
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing64
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

@Composable
fun ScanShoppingListScreen(
    navRouter: INavigationRouter,
    route: Destination.ScanShoppingListScreen
) {
    val context = LocalContext.current

    var hasCameraPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    )}

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    val viewModel = hiltViewModel<ScanShoppingListViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        viewModel.loadShoppingListData(route.shoppingListId)
    }
    if (state.hasAddedItems) {
        navRouter.returnBack()
        viewModel.reset()
    }

    BaseScreen(
        topBarText = "Scan Shopping List",
        onBackClick = { navRouter.returnBack() },
        showLoading = state.isLoading
    ) {
        ScanShoppingListScreenContent(it, state, viewModel)
    }
}

@Composable
private fun ScanShoppingListScreenContent(
    paddingValues: PaddingValues,
    state: ScanShoppingListUIState,
    actions: ScanShoppingListActions
) {
    var openedResult by remember { mutableStateOf(false) }

    val textAnalyzer by remember {
        mutableStateOf<ImageAnalysis.Analyzer>(TextRecognizer {
            if (openedResult) return@TextRecognizer
            actions.onTextScanned(it)
        })
    }

    if (openedResult) {
        Dialog(
            onDismissRequest = { openedResult = false }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing16),
                    modifier = Modifier.padding(spacing32)
                ) {
                    Text(text = "Scan Result")
                    LazyColumn {
                        items(items = state.scannedTextToShoppingItems()) {
                            Row {
                                Text(
                                    text = it.itemName,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(text = "${it.amount}x")
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                openedResult = false
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = textPrimaryColor())
                        ) {
                            Text("Try Again")
                        }
                        TextButton(
                            onClick = {
                                openedResult = false
                                actions.addScannedItemsToShoppingList()
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }

    CameraComposeView(
        paddingValues = paddingValues,
        analyzer = textAnalyzer
    ) {
        IconButton(
            onClick = { openedResult = true },
            modifier = Modifier.align(Alignment.BottomCenter).size(spacing64),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = backgroundSecondaryColor(),
                disabledContentColor = textSecondaryColor()
            ),
            enabled = state.scannedText.isNotEmpty()
        ) {
            Icon(imageVector = Lucide.Camera, contentDescription = null)
        }
    }
}