package com.example.expensemanager.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatDate(timeStamp: Long): String {
    val date = Date(timeStamp)

    val dateTime = SimpleDateFormat("dd MMM yyyy, HH:mm a", Locale.getDefault())
        .format(date)

    return dateTime
}