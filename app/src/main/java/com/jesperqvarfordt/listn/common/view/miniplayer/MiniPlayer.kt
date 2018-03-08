package com.jesperqvarfordt.listn.common.view.miniplayer

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.common.extensions.setVisible
import com.jesperqvarfordt.listn.common.view.miniplayer.di.DaggerMiniPlayerComponent
import com.jesperqvarfordt.listn.common.view.miniplayer.di.MiniPlayerModule
import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.player.PlayerActivity
import kotlinx.android.synthetic.main.view_mini_player.view.*
import javax.inject.Inject

class MiniPlayer
@JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
        RelativeLayout(context, attrs, defStyleAttr), MiniPlayerContract.View {

    @Inject
    lateinit var presenter: MiniPlayerContract.Presenter

    init {
        LayoutInflater.from(context).inflate(R.layout.view_mini_player, this)
        setVisible(false)
        DaggerMiniPlayerComponent.builder()
                .appComponent(App.instance.appComponent)
                .miniPlayerModule(MiniPlayerModule())
                .build()
                .inject(this)

        playPause.setOnClickListener({
            presenter.playPausedClicked()
        })

        setOnClickListener({
            presenter.miniPlayerClicked()
        })
    }

    override fun openMusicPlayer() {
        context.startActivity(Intent(context, PlayerActivity::class.java))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    override fun updateUi(info: CombinedInfo) {
        setVisible(true)
        playPause.isChecked = info.isPlaying
        title.text = info.title
    }
}