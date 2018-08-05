package com.jesperqvarfordt.listn.common.view.shuffletogglebutton

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.ImageView
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.common.view.shuffletogglebutton.di.DaggerShuffleToggleButtonComponent
import com.jesperqvarfordt.listn.common.view.shuffletogglebutton.di.ShuffleToggleButtonModule
import javax.inject.Inject

class ShuffleToggleButton
@JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
        ImageView(context, attrs, defStyleAttr), ShuffleToggleButtonContract.View {

    @Inject
    lateinit var presenter: ShuffleToggleButtonContract.Presenter

    init {
        DaggerShuffleToggleButtonComponent.builder()
                .appComponent(App.instance.appComponent)
                .shuffleToggleButtonModule(ShuffleToggleButtonModule())
                .build()
                .inject(this)
        setImageDrawable(context.getDrawable(R.drawable.ic_baseline_shuffle_24dp))
        setColorFilter(context.resources.getColor(R.color.progress_grey), PorterDuff.Mode.SRC_IN)
        setOnClickListener { presenter.shuffleClicked() }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    override fun toggleView(shuffling: Boolean) {
        if (shuffling) {
            setColorFilter(context.resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
        } else {
            setColorFilter(context.resources.getColor(R.color.progress_grey), PorterDuff.Mode.SRC_IN)
        }
    }
}