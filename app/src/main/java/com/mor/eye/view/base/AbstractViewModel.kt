package com.mor.eye.view.base

abstract class AbstractViewModel : RxViewModel() {

    abstract fun refresh()

    abstract fun onListScrolledToEnd()
}