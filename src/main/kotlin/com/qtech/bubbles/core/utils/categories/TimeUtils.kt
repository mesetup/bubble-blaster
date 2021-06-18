package com.qtech.bubbles.core.utils.categories

import java.time.LocalDateTime
import java.time.ZoneOffset

object TimeUtils {
    /**
     * @param duration the desired duration to format.
     * @return the formatten duration.
     */
    @JvmStatic
    fun formatDuration(duration: Long): String {
        /*
# ################################################### #

import time


def format_duration(duration: int):
    g = time.gmtime(duration)
    minute = g.tm_min
    second = g.tm_sec
    hour = duration / 60 / 60
    hour -= minute / 60
    hour -= second / 60 / 60
    hour = int(hour)
    minute1: str = str(minute)
    second1: str = str(second)
    if len(minute1) == 1:
        # noinspection PyTypeChecker
        minute1: str = "0" + minute1
    if len(second1) == 1:
        # noinspection PyTypeChecker
        second1: str = "0" + second1
    return f"{hour}:{minute1}:{second1}"

# ################################################### #
        */
        val g = LocalDateTime.ofEpochSecond(duration, 0, ZoneOffset.ofTotalSeconds(0))
        val minute = g.minute
        val second = g.second
        var hourDouble = duration.toDouble() / 60 / 60
        hourDouble -= minute.toDouble() / 60
        hourDouble -= second.toDouble() / 60 / 60
        val hour = hourDouble.toInt()
        var minuteString = Integer.toString(minute)
        var secondString = Integer.toString(second)
        if (minuteString.length == 1) minuteString = "0$minuteString"
        if (secondString.length == 1) secondString = "0$secondString"
        return "$hour:$minuteString:$secondString"
    }
}