package com.halimjr11.eventora.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.halimjr11.eventora.data.db.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDAO
}