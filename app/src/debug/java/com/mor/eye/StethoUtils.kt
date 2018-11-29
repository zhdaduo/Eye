package com.mor.eye

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient

object StethoUtils {
    fun initStetho(app: Application) {
        Stetho.initialize(
                Stetho.newInitializerBuilder(app)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(app))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(app))
                        .build())
    }

    fun addStethoNetWork(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder.addNetworkInterceptor(StethoInterceptor())
    }
}