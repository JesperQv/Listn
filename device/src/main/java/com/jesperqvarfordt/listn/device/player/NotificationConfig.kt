package com.jesperqvarfordt.listn.device.player

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class NotificationConfig(val playDrawable: Int = -1,
                              val pauseDrawable: Int = -1,
                              val nextDrawable: Int = -1,
                              val previousDrawable: Int = -1,
                              val smallIconDrawable: Int = -1,
                              val colorResource: Int = -1,
                              val activityToOpenOnClick: Class<*>) : Parcelable