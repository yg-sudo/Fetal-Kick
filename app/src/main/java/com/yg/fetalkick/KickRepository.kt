package com.yg.fetalkick

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KickRepository(context: Context) {
    private val kickEntryDao = KickDatabase.getDatabase(context).kickEntryDao()

    suspend fun getEntryByDate(date: String): KickEntry? =
        withContext(Dispatchers.IO) { kickEntryDao.getEntryByDate(date) }

    suspend fun insertOrUpdate(entry: KickEntry) =
        withContext(Dispatchers.IO) { kickEntryDao.insertOrUpdate(entry) }
}