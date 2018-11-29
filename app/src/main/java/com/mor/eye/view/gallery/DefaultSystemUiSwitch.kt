package com.mor.eye.view.gallery

import kotlin.properties.Delegates

class DefaultSystemUiSwitch {
    private var uiStateChangeListener: ((Boolean) -> Unit?)? = null

    var isUiVisible: Boolean by Delegates.observable(true) { _, old, new ->
        if (old != new) {
            uiStateChangeListener?.invoke(new)
        }
    }

    fun setOnUiStateChangeListener(uiStateChangeListener: ((Boolean) -> Unit?)?) {
        this.uiStateChangeListener = uiStateChangeListener
    }
}