package com.mor.eye.util.databinding

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.mor.eye.util.glide.loadImageCircle
import com.mor.eye.util.glide.loadImageRound

@BindingAdapter("visibleIfNull")
fun visibleIfNull(view: View, target: String?) {
    view.visibility = if (target.isNullOrBlank()) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("visibleIfNotNull")
fun visibleIfNotNull(view: View, target: String?) {
    view.visibility = if (target.isNullOrBlank()) View.GONE else View.VISIBLE
}

@BindingAdapter("visibleIfTrue")
fun visibleIfTrue(view: View, isShow: Boolean) {
    view.visibility = if (isShow) View.VISIBLE else View.GONE
}

@BindingAdapter("imageCircle")
fun loadImageCircle(view: ImageView, url: String) {
    view.loadImageCircle(view, url)
}

@BindingAdapter("imageRound")
fun loadImageRound(view: ImageView, url: String) {
    view.loadImageRound(view, url, 10)
}

@BindingAdapter("removeIf")
fun removeIf(view: View, predicate: Boolean) {
    if (view.visibility != View.GONE && predicate) {
        view.visibility = View.GONE
    }
}

