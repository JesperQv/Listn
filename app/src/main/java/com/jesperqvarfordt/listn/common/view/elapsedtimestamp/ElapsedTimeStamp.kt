package com.jesperqvarfordt.listn.common.view.elapsedtimestamp

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.widget.TextView
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.common.view.elapsedtimestamp.di.DaggerElapsedTimeStampComponent
import com.jesperqvarfordt.listn.common.view.elapsedtimestamp.di.ElapsedTimeStampModule
import javax.inject.Inject

class ElapsedTimeStamp
@JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
        TextView(context, attrs, defStyleAttr), ElapsedTimeStampContract.View {

    @Inject
    lateinit var presenter: ElapsedTimeStampContract.Presenter

    init {
        DaggerElapsedTimeStampComponent.builder()
                .appComponent(App.instance.appComponent)
                .elapsedTimeStampModule(ElapsedTimeStampModule())
                .build()
                .inject(this)

        typeface = ResourcesCompat.getFont(context, R.font.raleway_bold)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    override fun updateText(text: String) {
        this.text = text
    }

}