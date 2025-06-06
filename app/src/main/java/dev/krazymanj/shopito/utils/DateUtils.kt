package dev.krazymanj.shopito.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {

        private const val DATE_FORMAT_CS = "dd. MM. yyyy"
        private const val DATE_FORMAT_EN = "yyyy/MM/dd"

        fun getDateString(unixTime: Long): String{
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = unixTime

            val format: SimpleDateFormat
            if (LanguageUtils.isLanguageCzech()){
                format = SimpleDateFormat(DATE_FORMAT_CS, Locale.GERMAN)
            } else {
                format = SimpleDateFormat(DATE_FORMAT_EN, Locale.ENGLISH)

            }
            return format.format(calendar.getTime())
        }

    }

}
