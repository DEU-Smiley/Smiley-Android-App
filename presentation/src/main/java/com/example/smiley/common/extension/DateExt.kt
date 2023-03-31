package com.example.smiley.common.extension

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @return String of "yyyy-mm-dd"
 */
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
