package com.jesperqvarfordt.listn.home

import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView

interface HomeContract {

    interface View: BaseView

    interface Presenter: BasePresenter<View> {
        fun viewDestroyed()
    }

}