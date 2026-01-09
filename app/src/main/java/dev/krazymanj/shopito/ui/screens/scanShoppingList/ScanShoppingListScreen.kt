package dev.krazymanj.shopito.ui.screens.scanShoppingList

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Camera
import com.composables.icons.lucide.Lucide
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.INavigationRouter
import dev.krazymanj.shopito.ui.elements.CameraComposeView
import dev.krazymanj.shopito.ui.elements.modal.ScanResultDialog
import dev.krazymanj.shopito.ui.elements.screen.BaseScreen
import dev.krazymanj.shopito.ui.theme.backgroundSecondaryColor
import dev.krazymanj.shopito.ui.theme.spacing64

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
        state.shoppingList?.let {
            ScanResultDialog(
                onDismissRequest = {
                    openedResult = false
                    capturedBitmap = null
                },
                onConfirm = {
                    openedResult = false
                    capturedBitmap = null
                    actions.addScannedItemsToShoppingList()
                },
                isScanning = state.isScanning,
                scanError = state.scanError,
                shoppingList = it,
                scannedItems = state.scannedItems
            )
        }
    }

    var isCapturingImage by remember { mutableStateOf(false) }

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
                isCapturingImage = false
            }
        ) {
            IconButton(
                onClick = {
                    isCapturingImage = true
                    it()
                },
                modifier = Modifier.align(Alignment.BottomCenter).size(spacing64),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = backgroundSecondaryColor()
                ),
                enabled = !isCapturingImage
            ) {
                if (isCapturingImage) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Icon(imageVector = Lucide.Camera, contentDescription = null)
                }
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