package com.jesperqvarfordt.listn.device.imagecache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.jesperqvarfordt.listn.domain.model.Track
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Completable
import io.reactivex.Observable
import java.lang.Exception

class ImageCache {

    private val cache: MutableMap<String, Bitmap> = mutableMapOf()

    fun preloadImages(tracks: List<Track>) {
        if (cache.size > maxSize) cache.clear()
        tracks.map {
            Picasso.get().load(it.largeImageUrl).into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null && it.largeImageUrl != null) {
                        cache[it.largeImageUrl!!] = bitmap
                    }
                }
            })
        }
    }

    fun loadAllImagesAndThenComplete(tracks: List<Track>): Completable {
        return Observable.fromIterable(tracks)
                .flatMapCompletable { track ->
                    loadImageAsync(track).onErrorComplete()
                }
    }

    private fun loadImageAsync(track: Track): Completable {
        return Completable.fromCallable {
            Picasso.get().load(track.largeImageUrl).into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null && track.largeImageUrl != null) {
                        cache[track.largeImageUrl!!] = bitmap
                    }
                }
            })
        }
    }

    fun getBitmapIfCached(url: String?): Bitmap? {
        return cache[url]
    }

    companion object {
        const val maxSize = 100
    }
}