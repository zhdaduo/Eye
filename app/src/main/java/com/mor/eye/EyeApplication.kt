package com.mor.eye

import android.app.Application
import android.content.ComponentCallbacks2
import com.bumptech.glide.Glide
import com.mor.eye.di.EyeApp
import com.mor.eye.di.remoteDataSourceModule
import com.mor.eye.util.DisplayManager
import com.mor.eye.util.ktx.DelegatesExt
import com.mor.eye.util.IMMLeaks
import com.mor.eye.util.glide.GlideApp
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import org.koin.android.ext.android.startKoin
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

open class EyeApplication : Application(), ComponentCallbacks2 {

    companion object {
        var instance: EyeApplication by DelegatesExt.notNullSingleValue()
        var refWatcher: RefWatcher by DelegatesExt.notNullSingleValue()
    }
    override fun onCreate() {
        super.onCreate()
        instance = this

        refWatcher = LeakCanary.install(instance)

        startKoin(instance, EyeApp + remoteDataSourceModule)

        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

        StethoUtils.initStetho(instance)

        IMMLeaks.fixFocusedViewLeak(instance)

        DisplayManager.init(instance)

        Debuger.enable()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            GlideApp.get(this).clearMemory()
        }
        GlideApp.get(this).trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }
}