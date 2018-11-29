package com.mor.eye.view.gallery

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class HackyViewPager(context: Context, attributeSet: AttributeSet?) : ViewPager(context, attributeSet) {

    constructor(context: Context) : this(context, null)

    override fun onInterceptTouchEvent(ev: MotionEvent?) =
            try {
                super.onInterceptTouchEvent(ev)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
}