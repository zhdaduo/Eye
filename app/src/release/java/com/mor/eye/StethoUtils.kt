package com.mor.eye

import android.app.Application
import okhttp3.OkHttpClient

object StethoUtils {
    fun initStetho(app: Application) {
    }

    fun addStethoNetWork(builder: OkHttpClient.Builder): OkHttpClient.Builder = builder
}