package com.mor.eye.repository.remote

import com.mor.eye.util.other.isNetworkConnected
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        when (isNetworkConnected()) {
            true -> {
                val response: Response = chain.proceed(request)
                val maxAge = 10
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .build()
            }
            false -> {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
                val response: Response = chain.proceed(request)
                val maxStale = 60 * 60 * 24 * 3

                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
            }
        }
    }
}