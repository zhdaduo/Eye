package com.mor.eye.repository.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class RecentSearch(@PrimaryKey val query: String)