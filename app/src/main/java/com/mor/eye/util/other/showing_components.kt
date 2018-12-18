package com.mor.eye.util.other

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View

fun FragmentManager.commitTransaction(block: FragmentTransaction.() -> Unit) =
        beginTransaction().apply(block).commit()

fun Fragment.hideKeyboard() = activity?.hideKeyboard()

fun Activity.hideKeyboard() {
    currentFocus?.run {
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        currentFocus.clearFocus()
    }
}

fun Activity.finishNoAnim() {
    finish()
    overridePendingTransition(0, 0)
}

fun <T : View> Activity.bindable(@IdRes id: Int) = lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(id) }
fun <T : View> Fragment.bindable(@IdRes id: Int) = lazy(LazyThreadSafetyMode.NONE) { requireActivity().findViewById<T>(id) }