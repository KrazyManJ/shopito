package dev.krazymanj.shopito.ui.elements

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

@Composable
fun CameraComposeView(
    paddingValues: PaddingValues,
    analyzer: ImageAnalysis.Analyzer,
    overlay: @Composable BoxScope.() -> Unit
) {
    val context = LocalContext.current
    val processCameraProvider = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val mainExecutor = ContextCompat.getMainExecutor(context)
    val cameraProvider = processCameraProvider.get()
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val previewView = PreviewView(context)

                processCameraProvider.addListener({
                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    cameraProvider.unbindAll()

                    val analyzerBuilder = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    analyzerBuilder.setAnalyzer(cameraExecutor, analyzer)

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        analyzerBuilder,
                        preview
                    )

                }, mainExecutor)

                preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                return@AndroidView previewView
            }
        )
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            content = overlay
        )
    }
}