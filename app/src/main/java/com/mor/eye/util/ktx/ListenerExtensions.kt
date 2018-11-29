package com.mor.eye.util.ktx

import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver

class SimplePageChangeListener : ViewPager.OnPageChangeListener {
    private var scrolled: (Int, Float, Int) -> Unit = { _, _, _ -> }
    private var selected: (Int) -> Unit = {}
    private var scrollState: (Int) -> Unit = {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) =
            scrolled(position, positionOffset, positionOffsetPixels)

    override fun onPageSelected(position: Int) = selected(position)

    override fun onPageScrollStateChanged(state: Int) = scrollState(state)

    fun scrolled(scrolled: (Int, Float, Int) -> Unit) = apply { this.scrolled = scrolled }
    fun selected(selected: (Int) -> Unit) = apply { this.selected = selected }
    fun scrollState(scrollState: (Int) -> Unit) = apply { this.scrollState = scrollState }
}

fun onPageChangeListener() = SimplePageChangeListener()


inline fun RecyclerView.doOnScroll(
        crossinline action1: (recyclerView: RecyclerView?, dx: Int, dy: Int) -> Unit,
        crossinline action2: (recyclerView: RecyclerView?, newState: Int) -> Unit
) {

    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            action1(recyclerView, dx, dy)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            action2(recyclerView, newState)
        }
    })
}

/**
 * Performs the given action when the view tree is about to be drawn.
 */
inline fun View.doOnPreDraw(crossinline action: (view: View) -> Unit) {
    val vto = viewTreeObserver
    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            action(this@doOnPreDraw)
            when {
                vto.isAlive -> vto.removeOnPreDrawListener(this)
                else -> viewTreeObserver.removeOnPreDrawListener(this)
            }
            return true
        }
    })
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}