package dev.krazymanj.shopito.core.analyzers

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognizer(
    private val onTextDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)

            textRecognizer.process(image).addOnSuccessListener { text ->
                val resultText = buildString {
                    for (block in text.textBlocks) {
                        for (line in block.lines) {
                            append(line.text)
                            append("\n")
                        }
                    }
                }
                onTextDetected(resultText)

            } .addOnCompleteListener { imageProxy.close() }
        }
    }

}