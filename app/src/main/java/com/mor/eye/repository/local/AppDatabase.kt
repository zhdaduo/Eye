package com.mor.eye.repository.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [RecentSearch::class, CategoryOrder::class, ActiveTab::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun categoryOrderDao(): CategoryOrderDao
    abstract fun activeTabDao(): ActiveTabDao
}