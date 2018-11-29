package com.mor.eye.view.home.community

import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider

class CornerOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
        val rect = Rect()
        view?.getGlobalVisibleRect(rect)

        val selfRect = Rect(0, 0,
                rect.right - rect.left, view?.height!!)
        outline?.setRoundRect(selfRect, 10F)
    }
}