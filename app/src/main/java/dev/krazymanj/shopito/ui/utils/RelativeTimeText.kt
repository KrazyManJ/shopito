package dev.krazymanj.shopito.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.utils.DateUtils
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date
import java.util.Locale

@Composable
fun rememberRelativeTime(
    timeMillis: Long?,
    fallbackText: String = stringResource(R.string.unspecified_label),
): String {
    val configuration = LocalConfiguration.current

    val locale = remember(configuration) {
        configuration.locales[0] ?: Locale.getDefault()
    }

    val prettyTime = remember(locale) { PrettyTime(locale) }

    val todayLabel = stringResource(R.string.today_label)

    return remember(timeMillis, locale, todayLabel, fallbackText) {
        timeMillis?.let { millis ->
            val date = Date(millis)
            if (DateUtils.isToday(date)) {
                todayLabel
            } else {
                prettyTime.format(date)
            }
        } ?: fallbackText
    }
}