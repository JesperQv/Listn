package com.jesperqvarfordt.listn.common.extensions

import com.jesperqvarfordt.listn.domain.model.Track

fun MutableList<Track>.shuffleTo(index: Int): MutableList<Track> {
    for (i in 0 until index) {
        add(get(0))
        removeAt(0)
    }
    return this
}