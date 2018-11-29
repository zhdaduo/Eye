package com.mor.eye.repository.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class CategoryOrder(@PrimaryKey val order: Int, val category: String, val defaultIdx: Int)