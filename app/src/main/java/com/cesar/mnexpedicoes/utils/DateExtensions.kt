package com.cesar.mnexpedicoes.utils

import java.text.SimpleDateFormat
import java.util.*

fun String.formatToDateEnUs(): String {
    val inputDate = this.replace("/", "-")
    val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date as Date)
}