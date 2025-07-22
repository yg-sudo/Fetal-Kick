package com.yg.fetalkick

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [KickEntry::class], version = 1, exportSchema = false)
abstract class KickDatabase : RoomDatabase() {
    abstract fun kickEntryDao(): KickEntryDao

    companion object {
        @Volatile
        private var INSTANCE: KickDatabase? = null

        fun getDatabase(context: Context): KickDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KickDatabase::class.java,
                    "kick_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}