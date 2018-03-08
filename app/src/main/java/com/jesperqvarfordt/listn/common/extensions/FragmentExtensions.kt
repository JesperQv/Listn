package com.jesperqvarfordt.listn.common.extensions

import android.support.v4.app.Fragment

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}