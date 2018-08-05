package com.jesperqvarfordt.listn.common.view.repeattogglebutton

import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView

interface RepeatToggleButtonContract {

    interface View : BaseView {
        fun showRepeatNone()
        fun showRepeatAll()
        fun showRepeatOne()
    }

    interface Presenter : BasePresenter<View> {
        fun repeatClicked()
    }
}