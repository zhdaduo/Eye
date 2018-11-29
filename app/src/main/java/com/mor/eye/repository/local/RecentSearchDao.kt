package com.mor.eye.repository.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM recentsearch")
    fun getRecentSearch(): Single<List<RecentSearch>>

    @Insert(onConflict = IGNORE)
    fun insertSearch(recentSearch: RecentSearch)

    @Query("DELETE FROM recentsearch")
    fun deleteSearch()
}