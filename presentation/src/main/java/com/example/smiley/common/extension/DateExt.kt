package com.example.smiley.common.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @return String of "yyyy-mm-dd"
 */
@SuppressLint("SimpleDateFormat")
fun Date.toDateString(): String {
    return SimpleDateFormat("yyyy-mm-dd").format(this)
}

/**
 * @return String of "yyyy-mm-dd"
 */
fun String.toDateString(): String {
    return LocalDate.parse(this, DateTimeFormatter.ISO_DATE).toString()
}

/**
 * @return LocalDate of "yyyy-mm-dd"
 */
fun String.toDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
}

/**
 * yyMMdd To yyyy-MM-dd
 * @return LocalDate "yyyy-MM-dd"
 */
fun String.toDateOfyyMMdd(): LocalDate {
    val twoDigitYear = this.substring(0, 2).toInt()
    val year =
        if (twoDigitYear in 0..99) {
            if (twoDigitYear in 0..22) {
                2000 + twoDigitYear
            } else {
                1900 + twoDigitYear
            }
        } else {
            throw IllegalArgumentException("Invalid year format")
        }

    val month = this.substring(2, 4).toInt()
    val day = this.substring(4, 6).toInt()

    return LocalDate.of(year, month, day)
}

fun Calendar.getTodayOfWeek(): Int {
    return this.get(Calendar.DAY_OF_WEEK)
}