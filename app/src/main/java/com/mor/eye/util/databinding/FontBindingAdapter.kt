package com.mor.eye.util.databinding

import android.content.Context
import android.databinding.BindingConversion
import android.graphics.Typeface
import android.util.ArrayMap
import android.databinding.BindingAdapter
import android.widget.TextView
import com.mor.eye.EyeApplication
import com.mor.eye.R

@BindingAdapter("textStyle")
fun setTypeface(v: TextView, style: String) {
    when (style) {
        "bold" -> v.typeface = convertStringToFace(EyeApplication.instance.getString(R.string.lan_ting_bold))
        else -> v.typeface = convertStringToFace(EyeApplication.instance.getString(R.string.lan_ting_light))
    }
}

@BindingConversion
fun convertStringToFace(fontName: String): Typeface {
    try {
        return getTypeface(fontName, EyeApplication.instance)!!
    } catch (e: Exception) {
        throw e
    }
}

private val fontCache = ArrayMap<String, Typeface>()

fun getTypeface(fontName: String, context: Context): Typeface? {
    var tf = fontCache[fontName]
    if (tf == null) {
        try {
            tf = Typeface.createFromAsset(context.assets, fontName)
        } catch (e: Exception) {
            return null
        }
        fontCache[fontName] = tf
    }
    return tf
}