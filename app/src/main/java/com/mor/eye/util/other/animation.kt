package com.mor.eye.util.other

import android.view.View

fun View.fadeSlideUp() = slide(0f, -height.toFloat())

fun View.revealSlide() = slide(1f, 0f)

fun View.fadeSlideDown() = slide(0f, height.toFloat())

private fun View.slide(alpha: Float, translationY: Float) {
    animate()
            .alpha(alpha)
            .translationY(translationY)
}