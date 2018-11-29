package com.mor.eye.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.other.PlatformUtils

class ToolbarAlphaScrollBehavior : CoordinatorLayout.Behavior<android.support.v7.widget.Toolbar> {

    private var mStatusBarColorDrawable: ColorDrawable
    private var mStatusBarColor: Int = 0
    private var mTitleView: TextView? = null
    private var searchedForTitleView = false
    private var context: Context

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mStatusBarColor = DisplayUtils.getColor(context, R.color.colorPrimaryDark)
        mStatusBarColor = getColorWithAlpha(0f, mStatusBarColor)
        mStatusBarColorDrawable = ColorDrawable(mStatusBarColor)
        this.context = context
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: Toolbar, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: Toolbar, ev: MotionEvent?): Boolean {
        return ev == null || super.onInterceptTouchEvent(parent, child, ev)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: Toolbar, dependency: View): Boolean {
        if (dependency is AppBarLayout) {
            val ratio = getCurrentScrollValue(child, dependency).toFloat() / getTotalScrollRange(child, dependency)
            val alpha = 1f - Math.min(1f, Math.max(0f, ratio))
            val drawableAlpha = (alpha * 255).toInt()
            //  Log.i("ToolbarAlphaScrollBehavior", "Alpha: " + alpha);
            if (PlatformUtils.isLOLLIPOP() || PlatformUtils.isLOLLIPOP_MR1()) {
                child.background.alpha = drawableAlpha
            } else if (PlatformUtils.isKitkat()) {
                val toolbarParent = child.parent as ViewGroup
                if (toolbarParent.childCount == 2) {
                    val count = toolbarParent.childCount
                    for (i in count - 1 downTo 0) {
                        toolbarParent.getChildAt(i).background.alpha = drawableAlpha
                    }
                }
            } else {
                child.background.alpha = drawableAlpha
            }

            setStatusBarColor(parent, drawableAlpha)

            mTitleView?.alpha = alpha

            if (!searchedForTitleView) {
                val textView = TextView(context)
                textView.text = child.title
                mTitleView = textView
                searchedForTitleView = true
            }
        }
        return false
    }

    private fun setStatusBarColor(parent: CoordinatorLayout, alpha: Int) {
        val statusBarBackground = parent.statusBarBackground as ColorDrawable?
        statusBarBackground!!.color = getColorWithAlpha(alpha.toFloat(), statusBarBackground.color)
        parent.statusBarBackground = statusBarBackground
    }

    private fun getCurrentScrollValue(child: Toolbar, dependency: View): Int {
        return dependency.bottom - child.top
    }

    private fun getTotalScrollRange(child: Toolbar, dependency: View): Float {
        return (Math.max(dependency.height, (dependency as AppBarLayout).totalScrollRange) - child.top).toFloat()
    }

    companion object {

        private val TAG = ToolbarAlphaScrollBehavior::class.java.simpleName

        fun getColorWithAlpha(alpha: Float, baseColor: Int): Int {
            val a = Math.min(255, Math.max(0, (alpha * 255).toInt())) shl 24
            val rgb = 0x00ffffff and baseColor
            return a + rgb
        }
    }
}