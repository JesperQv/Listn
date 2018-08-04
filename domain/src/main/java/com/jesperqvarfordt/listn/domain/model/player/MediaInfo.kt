package com.jesperqvarfordt.listn.domain.model.player

data class MediaInfo(val id: Int,
                     val title: String?,
                     val artist: String?,
                     val coverUrl: String?,
                     val durationInMs: Int)