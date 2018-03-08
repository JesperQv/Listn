package com.jesperqvarfordt.listn.common.extensions

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar

fun ProgressBar.updateAnimator(progressAnimator: ValueAnimator,
                               isPlaying: Boolean,
                               currentPos: Int,
                               maxPos: Int) {
    max = maxPos
    progress = currentPos

    progressAnimator.cancel()

    val timeToEnd = maxPos - currentPos
    if (isPlaying && timeToEnd >= 0) {
        progressAnimator.setIntValues(currentPos, maxPos)
        progressAnimator.duration = timeToEnd.toLong()
        progressAnimator.interpolator = LinearInterpolator()
        progressAnimator.start()
    }
}