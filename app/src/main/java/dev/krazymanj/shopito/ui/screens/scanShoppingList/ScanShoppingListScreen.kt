package dev.krazymanj.shopito.ui.screens.scanShoppingList

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Camera
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.CameraComposeView
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing64
import dev.krazymanj.shopito.ui.theme.textPrimaryColor

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
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var openedResult by remember { mutableStateOf(false) }

    if (openedResult) {
        Dialog(
            onDismissRequest = {
                openedResult = false
                capturedBitmap = null
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing16),
                    modifier = Modifier.padding(spacing32)
                ) {
                    if (state.isScanning) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(spacing64)
                        ) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center) )
                        }
                    }
                    else if (state.scanError != null) {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = spacing16)) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(spacing16),
                            ) {
                                Icon(
                                    imageVector = Lucide.CircleAlert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(
                                    text = stringResource(state.scanError),
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                    else {
                        Text(text = "Scan Result")
                        LazyColumn {
                            items(items = state.scannedItems) {
                                Row {
                                    Text(
                                        text = it.itemName,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(text = "${it.amount}x")
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (!state.isScanning) {
                            TextButton(
                                onClick = {
                                    openedResult = false
                                    capturedBitmap = null
                                },
                                colors = ButtonDefaults.textButtonColors(contentColor = textPrimaryColor())
                            ) {
                                Text("Try Again")
                            }
                        }
                        if (!state.isScanning && state.scanError == null) {
                            TextButton(
                                onClick = {
                                    openedResult = false
                                    capturedBitmap = null
                                    actions.addScannedItemsToShoppingList()
                                },
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Add items")
                            }
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraComposeView(
            paddingValues = paddingValues,
            onImageCaptured = { imageProxy ->
                val rawBitmap = imageProxy.toBitmap()
                val rotation = imageProxy.imageInfo.rotationDegrees
                val finalBitmap = rotateBitmap(rawBitmap, rotation)
                actions.onImageCaptured(finalBitmap)
                capturedBitmap = finalBitmap
                imageProxy.close()
                openedResult = true
            }
        ) {
            IconButton(
                onClick = { it() },
                modifier = Modifier.align(Alignment.BottomCenter).size(spacing64),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(imageVector = Lucide.Camera, contentDescription = null)
            }
        }
        capturedBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Frozen Preview",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentScale = ContentScale.Crop
            )
        }
    }
}

fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
    if (rotationDegrees == 0) return bitmap
    val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}