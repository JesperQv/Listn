package com.jesperqvarfordt.listn.device.imagecache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.jesperqvarfordt.listn.domain.model.Track
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class ImageCache(private val context: Context) {

    private val cache: MutableMap<String, Bitmap> = mutableMapOf()

    fun preloadImages(tracks: List<Track>) {
        if (cache.size > maxSize) cache.clear()
        tracks.map {
            Picasso.with(context).load(it.largeImageUrl).into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapFailed(errorDrawable: Drawable?) {}

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null && it.largeImageUrl != null) {
                        cache.put(it.largeImageUrl!!, bitmap)
                    }
                }
            })
        }
    }

    fun getBitmapIfCached(url: String?): Bitmap? {
        return cache[url]
    }

    companion object {
        val maxSize = 100
    }
}