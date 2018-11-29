package com.mor.eye.repository.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
        val avatar: String,
        val name: String,
        val description: String,
        val isFollowed: Boolean,
        val likeNum: Int,
        val replyNum: Int,
        val tags: List<String>
) : Parcelable