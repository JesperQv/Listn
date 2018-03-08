package com.jesperqvarfordt.listn.domain.model

data class Track(val title: String?,
                 val artist: String?,
                 val thumbnailUrl: String?,
                 val largeImageUrl: String?,
                 val streamUrl: String?,
                 val durationInMs: Long)