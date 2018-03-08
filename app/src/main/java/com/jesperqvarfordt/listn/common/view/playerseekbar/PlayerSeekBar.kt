package com.jesperqvarfordt.listn.common.view.playerseekbar

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.common.extensions.updateAnimator
import com.jesperqvarfordt.listn.common.view.playerseekbar.di.DaggerPlayerSeekBarComponent
import com.jesperqvarfordt.listn.common.view.playerseekbar.di.PlayerSeekBarModule
import javax.inject.Inject

class PlayerSeekBar
@JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
        SeekBar(context, attrs, defStyleAttr), PlayerSeekBarContract.View, ValueAnimator.AnimatorUpdateListener {

    @Inject
    lateinit var presenter: PlayerSeekBarContract.Presenter
    private val progressAnimator: ValueAnimator = ValueAnimator()

    init {
        DaggerPlayerSeekBarComponent.builder()
                .appComponent(App.instance.appComponent)
                .playerSeekBarModule(PlayerSeekBarModule())
                .build()
                .inject(this)
        progressAnimator.addUpdateListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                presenter.startScrubbing()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                presenter.stopScrubbing(progress)
            }
        })
        presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    override fun seekTo(pos: Int) {
        progress = pos
    }

    override fun animateProgress(isPlaying: Boolean, currentPos: Int, maxPos: Int) {
        updateAnimator(progressAnimator, isPlaying, currentPos, maxPos)
    }

    override fun cancelAnimation() {
        progressAnimator.cancel()
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator?) {
        presenter.progressAnimationUpdated(valueAnimator?.animatedValue as Int)
    }
}