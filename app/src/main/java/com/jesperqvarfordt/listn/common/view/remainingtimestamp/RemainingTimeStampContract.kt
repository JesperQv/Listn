package com.jesperqvarfordt.listn.common.view.remainingtimestamp

import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView

interface RemainingTimeStampContract {

    interface View : BaseView {
        fun updateText(text: String)
    }

    interface Presenter : BasePresenter<View>
}