package com.halimjr11.eventora.domain.repository

import com.halimjr11.eventora.domain.model.EventDomain
import kotlinx.coroutines.flow.Flow

interface EventLocalRepository {
    fun isNotificationEnabled(): Boolean
    fun setNotificationEnabled(enabled: Boolean)
    fun getThemeKey(): String?
    fun setThemeKey(key: String)
    suspend fun insertEvent(event: EventDomain)

    fun getAllEvents(): Flow<List<EventDomain>>

    suspend fun checkIfEventExist(eventId: Int): Boolean

    suspend fun deleteEvent(event: EventDomain)

    suspend fun deleteAllEvents()
}