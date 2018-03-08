package com.jesperqvarfordt.listn.domain.model.player

data class CombinedInfo(private val playerInfo: PlayerInfo,
                        private val mediaInfo: MediaInfo) {

    val title: String? = mediaInfo.title
    val artist: String? = mediaInfo.artist
    val coverUrl: String? = mediaInfo.coverUrl
    val durationInMs: Int = mediaInfo.durationInMs
    val isPlaying: Boolean = playerInfo.isPlaying
    val elapsedTimeInMs: Int = playerInfo.elapsedTimeInMs

}