package dev.krazymanj.shopito.ui.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.utils.DateUtils
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date
import java.util.Locale

@Composable
fun PrettyTimeText(
    timeMillis: Long?,
    fallbackText: String = "X",
    formatUnit: ((value: String) -> String)? = null
) {
    val context = LocalContext.current
    val locale = getCurrentLocale(context)
    val prettyTime = PrettyTime(locale)

    val text = timeMillis?.let {
        val date = Date(it)
        if (DateUtils.isToday(date)) {
            stringResource(R.string.today_label)
        } else {
            prettyTime.format(date)
        }
    } ?: fallbackText

    Text(text = if (formatUnit != null) formatUnit(text) else text)
}

private fun getCurrentLocale(context: Context): Locale {
    val locales = context.resources.configuration.locales
    return if (!locales.isEmpty) locales[0] else Locale.getDefault()
}
