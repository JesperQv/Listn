package com.jesperqvarfordt.listn.common.view.playerprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.common.extensions.updateAnimator
import com.jesperqvarfordt.listn.common.view.playerprogressbar.di.DaggerPlayerProgressBarComponent
import com.jesperqvarfordt.listn.common.view.playerprogressbar.di.PlayerProgressBarModule
import javax.inject.Inject

class PlayerProgressBar
@JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
        ProgressBar(context, attrs, defStyleAttr), PlayerProgressBarContract.View, ValueAnimator.AnimatorUpdateListener {

    @Inject
    lateinit var presenter: PlayerProgressBarContract.Presenter
    private val progressAnimator: ValueAnimator = ValueAnimator()

    init {
        DaggerPlayerProgressBarComponent.builder()
                .appComponent(App.instance.appComponent)
                .playerProgressBarModule(PlayerProgressBarModule())
                .build()
                .inject(this)
        progressAnimator.addUpdateListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
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

    override fun onAnimationUpdate(valueAnimator: ValueAnimator?) {
        presenter.progressAnimationUpdated(valueAnimator?.animatedValue as Int)
    }


}