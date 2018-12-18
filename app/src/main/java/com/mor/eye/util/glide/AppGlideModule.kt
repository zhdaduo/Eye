package com.mor.eye.util.glide

import android.app.ActivityManager
import android.content.Context
import android.support.annotation.NonNull
import android.util.DisplayMetrics
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import java.io.InputStream

@GlideModule
class AppGlideModule : AppGlideModule() {
    /**
     * 通过GlideBuilder设置默认的结构(Engine,BitmapPool ,ArrayPool,MemoryCache等等).
     *
     * @param context
     * @param builder
     */
    override fun applyOptions(context: Context?, builder: GlideBuilder?) {
        val activityManager = context?.activityManager
        if (null != activityManager) {
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            val decodeFormat = if (memoryInfo.lowMemory) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888
            builder?.setDefaultRequestOptions(RequestOptions().format(decodeFormat))
        }
        //重新设置内存限制
        builder?.setMemoryCache(LruResourceCache(10 * 1024 * 1024))
    }

    /**
     * 清单解析的开启
     *
     *
     * 这里不开启，避免添加相同的modules两次
     *
     * @return
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    /**
     *
     * 为App注册一个自定义的String类型的BaseGlideUrlLoader
     * @param context
     * @param glide
     * @param registry
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(String::class.java, InputStream::class.java, CustomBaseGlideUrlLoader.Factory())
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