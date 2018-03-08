package com.jesperqvarfordt.listn.common.base

interface BasePresenter<in T : BaseView> {
    fun subscribe(view: T)
    fun unsubscribe()
}