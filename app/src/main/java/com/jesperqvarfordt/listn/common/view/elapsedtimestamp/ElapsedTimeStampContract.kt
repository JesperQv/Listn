package com.jesperqvarfordt.listn.common.view.elapsedtimestamp

import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView

interface ElapsedTimeStampContract {

    interface View : BaseView {
        fun updateText(text: String)
    }

    interface Presenter : BasePresenter<View>

}