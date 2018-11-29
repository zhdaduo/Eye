package com.mor.eye.repository.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface CategoryOrderDao {
    @Query("SELECT * FROM categoryorder")
    fun getCategoryOrder(): List<CategoryOrder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateCategory(categoryOrders: List<CategoryOrder>)
}