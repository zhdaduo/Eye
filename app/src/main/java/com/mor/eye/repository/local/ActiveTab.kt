package com.mor.eye.repository.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class ActiveTab(val tabId: Int, @PrimaryKey val tabTitle: String)