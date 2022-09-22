package com.example.food2go.utilities.helpers

import android.content.Context
import android.os.Build
import android.util.Log
import com.example.food2go.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun getTimeStampDifference(context: Context, doubleTimestamp: Long): String {
    val cal = Calendar.getInstance()
    val currentTime = cal.time
    val timeBefore = getDateFromTimestamp(doubleTimestamp)
    val difference = currentTime.time - timeBefore.time
    val days = (difference / (1000 * 60 * 60 * 24)).toInt()
    val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
    val min =
        (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
    val sec = TimeUnit.MILLISECONDS.toSeconds(difference).toInt()
    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val timeDisplay: String = when {
        days > 28 -> {
            Log.d("TIMESTAMP", timeBefore.toString())
            df.format(timeBefore)
        }
        days > 1 -> {
            days.toString() + " " + context.getString(R.string.days_ago)
        }
        days > 0 -> {
            days.toString() + " " + context.getString(R.string.day_ago)
        }
        hours > 1 -> {
            hours.toString() + " " + context.getString(R.string.hours_ago)
        }
        hours > 0 -> {
            hours.toString() + " " + context.getString(R.string.hour_ago)
        }
        min > 1 -> {
            min.toString() + " " + context.getString(R.string.minutes_ago)
        }
        min > 0 -> {
            min.toString() + " " + context.getString(R.string.minute_ago)
        }
        else -> {
            context.getString(R.string.few_seconds_ago)
        }
    }
    return timeDisplay
}

fun getDateFromTimestamp(doubleTimestamp: Long): Date {

    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = doubleTimestamp
    if (cal[Calendar.YEAR] == 1970) {
        cal.timeInMillis = doubleTimestamp* 1000L
    }
    return cal.time
}

fun formatDateString(createdAt: String) : String { //2022-09-07T06:59:46.000000Z -> Thu Jan 1 1970 07:30:00 GMT+0730
    return createdAt.toDate().formatTo("EEE MMM dd HH:mm:ss z yyyy")
}

fun String.toDate(
    dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
    timeZone: TimeZone = TimeZone.getTimeZone("Asia/Manila"),
): Date {
    val parser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(dateFormat, Locale.getDefault())
    } else {
        TODO("VERSION.SDK_INT < N")
    }
    parser.timeZone = timeZone
    return parser.parse(this)!!
}

fun Date.formatTo(
    dateFormat: String,
    timeZone: TimeZone = TimeZone.getTimeZone("Asia/Manila"),
): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}
