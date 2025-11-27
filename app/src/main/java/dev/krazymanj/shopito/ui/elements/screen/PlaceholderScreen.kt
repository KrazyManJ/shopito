package dev.krazymanj.shopito.ui.elements.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.krazymanj.shopito.ui.theme.Emphasized
import dev.krazymanj.shopito.ui.theme.Primary
import dev.krazymanj.shopito.ui.theme.backgroundPrimaryColor
import dev.krazymanj.shopito.ui.theme.spacing16
import dev.krazymanj.shopito.ui.theme.spacing32
import dev.krazymanj.shopito.ui.theme.spacing8
import dev.krazymanj.shopito.ui.theme.textPrimaryColor
import dev.krazymanj.shopito.ui.theme.textSecondaryColor

data class PlaceholderScreenContent(
    val icon: ImageVector? = null,
    val title: String? = null,
    val text: String? = null
)

@Composable
fun PlaceHolderScreen(
    content: PlaceholderScreenContent,
    modifier: Modifier = Modifier,
){
    Box(modifier = modifier.background(backgroundPrimaryColor()).fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(spacing16)
                .alpha(0.8f)
        ) {
            content.icon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                    tint = Primary
                )
                Spacer(modifier = Modifier.height(spacing32))
            }


            content.title?.let {
                Text(
                    text = content.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Emphasized,
                    textAlign = TextAlign.Center,
                    color = textPrimaryColor()
                )
            }

            content.text?.let {
                Spacer(modifier = Modifier.height(spacing8))
                Text(
                    text = content.text,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = textSecondaryColor(),
                    modifier = Modifier.padding(horizontal = spacing32)
                )
            }
        }
    }
}