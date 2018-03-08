package com.jesperqvarfordt.listn.common.extensions

import android.content.res.Resources

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Long.msToTimeStamp(): String {
    val temp = this / 1000
    val minutes = temp / 60
    var minuteString = minutes.toString()
    if (minutes < 10) {
        minuteString = "0" + minuteString
    }

    val seconds = temp % 60
    var secondString = seconds.toString()
    if (seconds < 10) {
        secondString = "0" + secondString
    }
    return minuteString + ":" + secondString
}