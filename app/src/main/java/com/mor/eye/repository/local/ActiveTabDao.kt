package com.mor.eye.repository.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface ActiveTabDao {
    @Query("SELECT * FROM activetab")
    fun getActiveTab(): Single<List<ActiveTab>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertActiveTab(recentSearch: ActiveTab)

    @Query("DELETE FROM activetab")
    fun deleteActiveTab()
}