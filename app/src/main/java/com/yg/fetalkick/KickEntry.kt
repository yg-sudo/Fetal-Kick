package com.yg.fetalkick

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kick_entries")
data class KickEntry(
    @PrimaryKey val date: String, // Format: YYYY-MM-DD
    val breakfastCount: Int = 0,
    val lunchCount: Int = 0,
    val snacksCount: Int = 0,
    val dinnerCount: Int = 0
)