package com.jesperqvarfordt.listn.domain.model.player

data class PlayerInfo(val isPlaying: Boolean,
                      val elapsedTimeInMs: Int,
                      val shuffleMode: ShuffleMode = ShuffleMode.SHUFFLE_NONE,
                      val repeatMode: RepeatMode = RepeatMode.REPEAT_ALL)