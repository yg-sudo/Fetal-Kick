package com.yg.fetalkick

import androidx.room.*

@Dao
interface KickEntryDao {
    @Query("SELECT * FROM kick_entries WHERE date = :date LIMIT 1")
    suspend fun getEntryByDate(date: String): KickEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: KickEntry)
}