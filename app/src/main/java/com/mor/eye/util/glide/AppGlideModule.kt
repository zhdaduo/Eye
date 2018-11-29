package com.mor.eye.util.glide

import android.app.ActivityManager
import android.content.Context
import android.support.annotation.NonNull
import android.util.DisplayMetrics
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class AppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context?, builder: GlideBuilder?) {
        val activityManager = context?.activityManager
        if (null != activityManager) {
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            val decodeFormat = if (memoryInfo.lowMemory) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888
            builder?.setDefaultRequestOptions(RequestOptions().format(decodeFormat))
        }
        val calculator = MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2F)
                .build()
        val memoryCacheSize = calculator.memoryCacheSize
        builder?.setMemoryCache(LruResourceCache(memoryCacheSize.toLong()))
    }
}

val Context.activityManager: ActivityManager
    get() =
        getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

@GlideExtension
object MyGlideExtension {

    @NonNull
    @GlideOption
    @JvmStatic
    fun roundedCorners(options: RequestOptions, @NonNull ctx: Context, cornerRadius: Int): RequestOptions {
        val px = Math.round(cornerRadius * ctx.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)
        return options.transforms(RoundedCorners(px))
    }
}