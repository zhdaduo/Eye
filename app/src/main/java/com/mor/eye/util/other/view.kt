package com.mor.eye.util.other

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.support.annotation.LayoutRes
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.mor.eye.EyeApplication
import com.mor.eye.R

val View.ctx: Context
    get() = context

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

inline fun View.showIf(predicate: () -> Boolean): View {
    if (visibility != View.VISIBLE && predicate()) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.remove(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun View.removeIf(predicate: Boolean): View {
    if (visibility != View.GONE && predicate) {
        visibility = View.GONE
    }
    return this
}

fun getDrawable(rgb: Int, radius: Float): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.apply {
        setColor(rgb)
        gradientType = GradientDrawable.RECTANGLE
        cornerRadius = radius
        setStroke(EyeApplication.instance.dpToPx(1), rgb)
    }
    return gradientDrawable
}

fun getSelector(normalDrawable: Drawable, pressDrawable: Drawable): StateListDrawable {
    val stateListDrawable = StateListDrawable()
    stateListDrawable.apply {
        addState(intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed), pressDrawable)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), normalDrawable)
        addState(intArrayOf(), normalDrawable)
    }
    return stateListDrawable
}

fun View.hideKeyboard(): Boolean {
    clearFocus()
    return (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard(): Boolean {
    requestFocus()
    return (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun startAnimation(context: Context, view: ImageView) {
    val animation = AnimationUtils.loadAnimation(context, R.anim.item_alpha)
    animation.fillAfter = true
    view.startAnimation(animation)
}

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) { visibility = if (value) View.VISIBLE else View.GONE }