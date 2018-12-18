package com.mor.eye.util.other

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.annotation.StringRes
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.mor.eye.EyeApplication
import kotlin.reflect.KClass

fun Context.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
fun Context.showToast(@StringRes msgId: Int) = Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show()

fun Context.startActivity(clazz: KClass<*>, intentFun: Intent.() -> Unit) =
        startActivity(Intent(this, clazz.java).apply(intentFun))

val Context.inputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Context.connectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.startKActivity(clazz: KClass<*>, intentFun: Intent.() -> Unit) =
        startActivity(Intent(this, clazz.java).apply(intentFun))

fun Context.getDisplayMetrics(): DisplayMetrics {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return DisplayMetrics().apply {
        wm.defaultDisplay.getRealMetrics(this)
    }
}

fun Context.density() = getDisplayMetrics().density
fun Context.dpToPx(dp: Int) = Math.round(dp.toFloat() * density())

fun isNetworkConnected(): Boolean {
    val connectivityManager = EyeApplication.instance.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return connectivityManager.activeNetworkInfo != null
}