package com.example.expensemanager.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatDate(timeStamp: Long): String {
    val date = Date(timeStamp)
    val timezone = TimeZone.getDefault()

    val dateTime = SimpleDateFormat("dd MMM yyyy, HH:mm a", Locale.getDefault())
        .apply {
            this.timeZone = timezone
        }
        .format(date)

    val zoneName = timezone.getDisplayName(
        timezone.inDaylightTime(date),
        TimeZone.SHORT,
        Locale.ENGLISH
    )

    return buildString {
        append(dateTime)
        append(zoneName)
    }
}