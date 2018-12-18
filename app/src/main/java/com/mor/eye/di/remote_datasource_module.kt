package com.mor.eye.di

import com.mor.eye.BuildConfig
import com.mor.eye.EyeApplication
import com.mor.eye.StethoUtils
import com.mor.eye.di.DataSourceProperties.CACHE_SIZE
import com.mor.eye.di.DataSourceProperties.PATH_CACHE
import com.mor.eye.di.DataSourceProperties.SERVER_URL
import com.mor.eye.repository.EyeDataSource
import com.mor.eye.repository.remote.CacheInterceptor
import com.mor.eye.repository.remote.RxErrorHandlingCallAdapterFactory
import com.mor.eye.util.AppUtils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val remoteDataSourceModule = module {
    single(definition = { createOkHttpClient() })
    single(definition = { createWebService<EyeDataSource>(get()) })
}

object DataSourceProperties {
    const val SERVER_URL = "http://baobab.kaiyanapp.com/api/"
    const val CACHE_SIZE = 50 * 1024 * 1024L
    val PATH_CACHE = EyeApplication.instance.cacheDir.absolutePath + File.separator + "data" + "/NetCache"
}

fun createOkHttpClient(): OkHttpClient {

    var builder = OkHttpClient.Builder()

    val cache = Cache(File(PATH_CACHE), CACHE_SIZE)

    if (BuildConfig.DEBUG) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(httpLoggingInterceptor)
    }

    builder.addInterceptor(addQueryParameterInterceptor())
    builder.addInterceptor(addHeaderInterceptor())
    builder.addInterceptor(CacheInterceptor())
    builder.addNetworkInterceptor(CacheInterceptor())
    builder = StethoUtils.addStethoNetWork(builder)

    builder.cache(cache)
    builder.connectTimeout(60L, TimeUnit.SECONDS)
    builder.readTimeout(60L, TimeUnit.SECONDS)
    builder.writeTimeout(60L, TimeUnit.SECONDS)

    builder.retryOnConnectionFailure(false)

    return builder.build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient): T {
    val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}

/**
 * 设置公共参数
 */
private fun addQueryParameterInterceptor(): Interceptor {
    return Interceptor { chain ->
        val originalRequest = chain.request()
        val request: Request
        val modifiedUrl = originalRequest.url().newBuilder()
                .addQueryParameter("udid", "5394cda42d75480cb47942affe0feb45339343a4")
                .addQueryParameter("deviceModel", AppUtils.getMobileModel())
                .build()
        request = originalRequest.newBuilder().url(modifiedUrl).build()
        chain.proceed(request)
    }
}

/**
 * 设置头
 */
private fun addHeaderInterceptor(): Interceptor {
    return Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
                //.header("token", token)
                .method(originalRequest.method(), originalRequest.body())
        val request = requestBuilder.build()
        chain.proceed(request)
    }
}