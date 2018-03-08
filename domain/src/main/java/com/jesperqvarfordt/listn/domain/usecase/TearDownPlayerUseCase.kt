package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer

open class TearDownPlayerUseCase(private val player: MusicPlayer) {

    open fun execute() = player.tearDown()
}