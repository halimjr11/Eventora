package com.halimjr11.eventora.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.halimjr11.eventora.data.db.entity.EventEntity
import com.halimjr11.eventora.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Query("SELECT * FROM ${Constants.ENTITY_NAME}")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM ${Constants.ENTITY_NAME} WHERE id = :eventId LIMIT 1")
    suspend fun getEventById(eventId: Int): EventEntity?

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("DELETE FROM ${Constants.ENTITY_NAME}")
    suspend fun deleteAllEvents()
}