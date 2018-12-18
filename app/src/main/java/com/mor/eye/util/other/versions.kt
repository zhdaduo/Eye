package com.mor.eye.util.other

import android.os.Build.VERSION_CODES
import android.os.Build.VERSION
import android.support.annotation.RestrictTo
import android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP

object VersionsUtils {
    @RestrictTo(LIBRARY_GROUP)
    fun isJellyBeanMR1() = VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1

    @RestrictTo(LIBRARY_GROUP)
    fun isKitKat() = VERSION.SDK_INT >= VERSION_CODES.KITKAT

    @RestrictTo(LIBRARY_GROUP)
    fun isLollipop() = VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP

    @RestrictTo(LIBRARY_GROUP)
    fun isEqualLollipop() = VERSION.SDK_INT == VERSION_CODES.LOLLIPOP

    @RestrictTo(LIBRARY_GROUP)
    fun isEqualLollipopMR1() = VERSION.SDK_INT == VERSION_CODES.LOLLIPOP_MR1

    @RestrictTo(LIBRARY_GROUP)
    fun isMarshmallow() = VERSION.SDK_INT >= VERSION_CODES.M
}