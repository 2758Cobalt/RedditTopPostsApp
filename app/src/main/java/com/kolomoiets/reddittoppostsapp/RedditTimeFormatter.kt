package com.kolomoiets.reddittoppostsapp

import android.content.Context
import java.util.concurrent.TimeUnit

class RedditTimeFormatter(private val context: Context) {
    private enum class TimeTypes { hour, day }

    fun formatTime(unixTime: Number): String {
        val currentTime = System.currentTimeMillis() / 1000
        val timeDifference = currentTime - unixTime.toLong()

        val hours = TimeUnit.SECONDS.toHours(timeDifference)
        val days = TimeUnit.SECONDS.toDays(timeDifference)

        return when {
            hours > 0 -> context.getString(R.string.post_dateTime).format(hours, TimeTypes.hour.name + (if (hours > 1) TimeTypes.hour.name + "s" else ""))
            days > 0 -> context.getString(R.string.post_dateTime).format(days, TimeTypes.day.name + (if (days > 1) TimeTypes.day.name + "s" else ""))
            else -> {"just now"}
        }
    }
}