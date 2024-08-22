package com.kolomoiets.reddittoppostsapp

import android.content.Context
import java.util.concurrent.TimeUnit

class RedditTimeFormatter(private val context: Context) {
    private enum class TimeTypes { minute, hour, day }

    fun formatTime(unixTime: Number): String {
        val currentTime = System.currentTimeMillis() / 1000
        val timeDifference = currentTime - unixTime.toLong()

        val days = TimeUnit.SECONDS.toDays(timeDifference)
        val hours = TimeUnit.SECONDS.toHours(timeDifference)
        val minutes = TimeUnit.SECONDS.toMinutes(timeDifference)

        return when {
            days > 0 -> context.getString(R.string.post_dateTime).format(days, if (days > 1) TimeTypes.day.name + "s" else TimeTypes.day.name)
            hours > 0 -> context.getString(R.string.post_dateTime).format(hours, if (hours > 1) TimeTypes.hour.name + "s" else TimeTypes.hour.name)
            minutes > 0 -> context.getString(R.string.post_dateTime).format(minutes, if (minutes > 1) TimeTypes.minute.name + "s" else TimeTypes.minute.name)
//
            else -> { context.getString(R.string.post_justNow) }
        }
    }
}