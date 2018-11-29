package com.mor.eye.ui

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
data class UiResource(val status: Status, internal val exception: Throwable? = null)