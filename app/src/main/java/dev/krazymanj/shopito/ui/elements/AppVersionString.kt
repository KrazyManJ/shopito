package dev.krazymanj.shopito.ui.elements

import android.content.pm.PackageInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.krazymanj.shopito.R

@Composable
fun AppVersionString(
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    val appInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

    Column(
        modifier = modifier.then(Modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.app_name))
        Text(stringResource(R.string.version)+": ${appInfo.versionName}")
    }
}