package com.mor.eye.util.other

import android.content.res.TypedArray

inline fun <T : TypedArray> T.use(block: T.() -> Unit) {
    try {
        block()
    } finally {
        recycle()
    }
}

fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
