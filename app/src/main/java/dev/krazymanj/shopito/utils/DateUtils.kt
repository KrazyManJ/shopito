package dev.krazymanj.shopito.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtils {

    companion object {

        private const val DATE_FORMAT_CS = "dd. MM. yyyy"
        private const val DATE_FORMAT_EN = "yyyy-MM-dd"

        private const val DATETIME_FORMAT_CS = "dd. MM. yyyy HH:mm"
        private const val DATETIME_FORMAT_EN = "yyyy-MM-dd HH:mm"

        fun getDateString(unixTime: Long): String{
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = unixTime
            val format: SimpleDateFormat = if (LanguageUtils.isLanguageCzech()){
                SimpleDateFormat(DATE_FORMAT_CS, Locale.GERMAN)
            } else {
                SimpleDateFormat(DATE_FORMAT_EN, Locale.ENGLISH)
            }
            return format.format(calendar.getTime())
        }

        fun getDateTimeString(unixTime: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = unixTime
            val format: SimpleDateFormat = if (LanguageUtils.isLanguageCzech()){
                SimpleDateFormat(DATETIME_FORMAT_CS, Locale.GERMAN)
            } else {
                SimpleDateFormat(DATETIME_FORMAT_EN, Locale.ENGLISH)

            }
            return format.format(calendar.getTime())
        }

        fun isToday(date: Date): Boolean {
            val calendarDate = Calendar.getInstance().apply { time = date }
            val calendarNow = Calendar.getInstance()

            return calendarDate.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR) &&
                    calendarDate.get(Calendar.DAY_OF_YEAR) == calendarNow.get(Calendar.DAY_OF_YEAR)
        }

        fun isPast(unixTime: Long): Boolean {
            val dateToCheck = Instant.ofEpochMilli(unixTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            val today = LocalDate.now(ZoneId.systemDefault())

            return dateToCheck.isBefore(today)
        }

    }

}
