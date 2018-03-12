package com.jesperqvarfordt.listn.dagger.module

import android.content.Context
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.device.imagecache.ImageCache
import com.jesperqvarfordt.listn.device.player.NotificationConfig
import com.jesperqvarfordt.listn.device.player.StreamingMusicPlayer
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.player.PlayerActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MusicPlayerModule {

    @Provides
    @Singleton
    fun notificationConfig(): NotificationConfig {
        return NotificationConfig(R.drawable.ic_play_arrow_white_24dp,
                R.drawable.ic_pause_white_24dp,
                R.drawable.ic_skip_next_white_24dp,
                R.drawable.ic_skip_previous_white_24dp,
                R.drawable.listn_small_icon,
                R.color.colorPrimary,
                PlayerActivity::class.java)
    }

    @Provides
    @Singleton
    fun musicPlayer(context: Context,
                    config: NotificationConfig,
                    imageCache: ImageCache): MusicPlayer {
        return StreamingMusicPlayer(context, config, imageCache)
    }

}