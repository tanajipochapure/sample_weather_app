package com.mphasis.tanaji.weatherapp.utilities

import java.text.SimpleDateFormat
import java.util.*

object Utilities {
    fun convertDate(date: Long): String? =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
}