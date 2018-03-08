package com.jesperqvarfordt.listn.common.view.remainingtimestamp

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.widget.TextView
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.common.view.remainingtimestamp.di.DaggerRemainingTimeStampComponent
import com.jesperqvarfordt.listn.common.view.remainingtimestamp.di.RemainingTimeStampModule
import javax.inject.Inject


class RemainingTimeStamp
@JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
        TextView(context, attrs, defStyleAttr), RemainingTimeStampContract.View {

    @Inject
    lateinit var presenter: RemainingTimeStampContract.Presenter

    init {
        DaggerRemainingTimeStampComponent.builder()
                .appComponent(App.instance.appComponent)
                .remainingTimeStampModule(RemainingTimeStampModule())
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