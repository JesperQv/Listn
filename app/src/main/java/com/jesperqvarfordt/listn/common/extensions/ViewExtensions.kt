package com.jesperqvarfordt.listn.common.extensions

import android.view.View

fun View.setVisible(visible: Boolean) {
    when (visible) {
        true -> visibility = View.VISIBLE
        false -> visibility = View.GONE
    }
}