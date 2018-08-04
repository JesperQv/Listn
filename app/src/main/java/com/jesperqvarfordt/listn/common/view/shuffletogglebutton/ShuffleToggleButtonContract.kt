package com.jesperqvarfordt.listn.common.view.shuffletogglebutton

import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView

interface ShuffleToggleButtonContract {

    interface View : BaseView {
        fun toggleView(shuffling: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun shuffleClicked()
    }
}