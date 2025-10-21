package com.halimjr11.eventora.data.repository

import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.data.db.EventDAO
import com.halimjr11.eventora.data.mapper.LocalDataMapper
import com.halimjr11.eventora.data.preferences.SharedPreferenceHelper
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.domain.repository.EventLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

class EventLocalRepositoryImpl(
    private val dao: EventDAO,
    private val mapper: LocalDataMapper,
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val dispatcher: CoroutineDispatcherProvider
) : EventLocalRepository {
    override fun isNotificationEnabled(): Boolean {
        return sharedPreferenceHelper.isNotificationEnabled()
    }

    override fun setNotificationEnabled(enabled: Boolean) {
        sharedPreferenceHelper.setNotificationEnabled(enabled)
    }

    override fun getThemeKey(): String? {
        return sharedPreferenceHelper.getThemeKey()
    }

    override fun setThemeKey(key: String) {
        sharedPreferenceHelper.setThemeKey(key)
    }

    override suspend fun insertEvent(event: EventDomain) = withContext(dispatcher.io) {
        dao.insertEvent(mapper.mapDomainToEntity(event))
    }

    override fun getAllEvents(): Flow<List<EventDomain>> {
        return dao.getAllEvents().transform {
            emit(it.map { event -> mapper.mapEntityToDomain(event) })
        }
    }

    override suspend fun checkIfEventExist(eventId: Int): Boolean = withContext(dispatcher.io) {
        val event = dao.getEventById(eventId)
        event != null
    }


    override suspend fun deleteEvent(event: EventDomain) = withContext(dispatcher.io) {
        dao.deleteEvent(mapper.mapDomainToEntity(event))
    }

    override suspend fun deleteAllEvents() = withContext(dispatcher.io) {
        dao.deleteAllEvents()
    }
}