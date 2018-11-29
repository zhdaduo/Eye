package com.mor.eye.repository.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShareBean(
        val coverUrl: String,
        val blurUrl: String,
        val forWeibo: String,
        val title: String,
        val description: String
) : Parcelable