package com.halimjr11.eventora.data.repository.impl

import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.data.mapper.EventDataMapper
import com.halimjr11.eventora.data.model.EventResponse
import com.halimjr11.eventora.data.repository.EventRepository
import com.halimjr11.eventora.data.service.EventService
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.utils.DomainResult
import kotlinx.coroutines.withContext

class EventRepositoryImpl(
    private val service: EventService,
    private val mapper: EventDataMapper,
    private val dispatcher: CoroutineDispatcherProvider
) : EventRepository {
    override suspend fun getAllEvents(): DomainResult<List<EventDomain>> = safeCall {
        service.getEvents().data?.map {
            mapper.mapEventResponseToDomain(it)
        }.orEmpty()
    }

    override suspend fun getUpcomingEvents(): DomainResult<List<EventDomain>> = safeCall {
        service.getEvents(active = 1).data?.map {
            mapper.mapEventResponseToDomain(it)
        }.orEmpty()
    }

    override suspend fun getPastEvents(): DomainResult<List<EventDomain>> = safeCall {
        service.getEvents(active = 0).data?.map { mapper.mapEventResponseToDomain(it) }.orEmpty()
    }

    override suspend fun searchEvents(keyword: String): DomainResult<List<EventDomain>> = safeCall {
        service.getEvents(keyword = keyword).data?.map {
            mapper.mapEventResponseToDomain(it)
        }.orEmpty()
    }

    override suspend fun getEventDetail(id: Int): DomainResult<EventDomain> = safeCall {
        val result = service.getEventDetail(id).data ?: EventResponse()
        mapper.mapEventResponseToDomain(result)
    }

    private suspend fun <T> safeCall(
        block: suspend () -> T
    ): DomainResult<T> = withContext(dispatcher.io) {
        try {
            DomainResult.Success(block())
        } catch (e: Exception) {
            DomainResult.Error
        }
    }
}