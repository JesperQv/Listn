package com.jesperqvarfordt.listn.data.mapper

import com.jesperqvarfordt.listn.data.model.SCTrack
import com.jesperqvarfordt.listn.domain.model.Track

open class TrackMapper(private val clientId: String) {

    fun map(from: SCTrack): Track {
        val largeImageUrl = from.coverUrl?.replace("large", "t500x500")
        return Track(from.id,
                from.title,
                from.artist?.name,
                from.coverUrl,
                largeImageUrl,
        from.streamUrl + "/stream" + "?client_id=" + clientId,
                from.durationInMs)
    }

}