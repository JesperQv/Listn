package com.jesperqvarfordt.listn.cast

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.jesperqvarfordt.listn.BuildConfig


internal class CastOptionsProvider : OptionsProvider {

    override fun getCastOptions(context: Context?): CastOptions {
        return CastOptions.Builder()
                .setReceiverApplicationId(BuildConfig.CAST_APP_ID)
                .build()
    }

    override fun getAdditionalSessionProviders(context: Context?): MutableList<SessionProvider> {
        return mutableListOf()
    }

}