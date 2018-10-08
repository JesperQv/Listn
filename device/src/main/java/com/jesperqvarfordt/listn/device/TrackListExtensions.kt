package com.jesperqvarfordt.listn.device

import android.support.v4.media.MediaMetadataCompat
import com.jesperqvarfordt.listn.device.imagecache.ImageCache
import com.jesperqvarfordt.listn.domain.model.Track

fun List<Track>.toMediaMetadata(imageCache: ImageCache): List<MediaMetadataCompat> {
    return map {
        MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, it.id.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, it.title)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, it.streamUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, it.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, it.largeImageUrl)
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, imageCache.getBitmapIfCached(it.largeImageUrl))
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it.durationInMs)
                .build()
    }
}

fun List<MediaMetadataCompat>.isSameTracks(tracks: List<Track>): Boolean {
    val list1 = mutableListOf<String?>()
    forEach { list1.add(it.description.mediaId) }
    val list2 = mutableListOf<String?>()
    tracks.forEach { list2.add(it.id.toString()) }
    return list1.containsAll(list2)
}