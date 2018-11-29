package com.mor.eye.repository.remote

import com.mor.eye.util.other.unsafeLazy
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory : CallAdapter.Factory() {

    private val _original by unsafeLazy {
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    companion object {
        fun create(): CallAdapter.Factory = RxErrorHandlingCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        val wrapped = _original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>
        return RxCallAdapterWrapper(retrofit, wrapped)
    }

    private class RxCallAdapterWrapper<R>(
            val _retrofit: Retrofit,
            val _wrappedCallAdapter: CallAdapter<R, *>
    ) : CallAdapter<R, Single<R>> {

        override fun responseType(): Type = _wrappedCallAdapter.responseType()

        @Suppress("UNCHECKED_CAST")
        override fun adapt(call: Call<R>): Single<R> {
            val adapted = (_wrappedCallAdapter.adapt(call) as Single<R>)
            return adapted.onErrorResumeNext { throwable: Throwable ->
                Single.error(asRetrofitException(throwable))
            }
        }

        private fun asRetrofitException(throwable: Throwable): RetrofitException {
            // We had non-200 http error
            if (throwable is HttpException) {
                val response = throwable.response()
                return RetrofitException.httpError(response.raw().request().url().toString(), response, _retrofit)
            }

            // A network error happened
            if (throwable is IOException) {
                return RetrofitException.networkError(throwable)
            }

            // We don't know what happened. We need to simply convert to an unknown error
            return RetrofitException.unexpectedError(throwable)
        }
    }
}