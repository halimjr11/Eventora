package com.halimjr11.eventora.domain.repository

import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.utils.DomainResult

interface EventRemoteRepository {
    suspend fun getAllEvents(): DomainResult<List<EventDomain>>
    suspend fun getNearestEvent(): EventDomain?
    suspend fun getUpcomingEvents(): DomainResult<List<EventDomain>>
    suspend fun getPastEvents(): DomainResult<List<EventDomain>>
    suspend fun searchEvents(keyword: String): DomainResult<List<EventDomain>>
    suspend fun getEventDetail(id: Int): DomainResult<EventDomain>
}