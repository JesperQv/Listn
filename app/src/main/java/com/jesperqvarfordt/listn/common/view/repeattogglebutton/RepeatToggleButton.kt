package com.jesperqvarfordt.listn.common.view.repeattogglebutton

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.common.view.repeattogglebutton.di.DaggerRepeatToggleButtonComponent
import com.jesperqvarfordt.listn.common.view.repeattogglebutton.di.RepeatToggleButtonModule
import javax.inject.Inject

class RepeatToggleButton
@JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
        ImageView(context, attrs, defStyleAttr), RepeatToggleButtonContract.View {

    @Inject
    lateinit var presenter: RepeatToggleButtonContract.Presenter

    init {
        DaggerRepeatToggleButtonComponent.builder()
                .appComponent(App.instance.appComponent)
                .repeatToggleButtonModule(RepeatToggleButtonModule())
                .build()
                .inject(this)
        setOnClickListener { presenter.repeatClicked() }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    override fun showRepeatNone() {
        setImageDrawable(context.getDrawable(R.drawable.ic_baseline_repeat_24dp))
        setColorFilter(context.resources.getColor(R.color.progress_grey))
    }

    override fun showRepeatAll() {
        setImageDrawable(context.getDrawable(R.drawable.ic_baseline_repeat_24dp))
        setColorFilter(context.resources.getColor(R.color.colorPrimary))
    }

    override fun showRepeatOne() {
        setImageDrawable(context.getDrawable(R.drawable.ic_baseline_repeat_one_24dp))
        setColorFilter(context.resources.getColor(R.color.colorPrimary))
    }
}