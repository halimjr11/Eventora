package com.halimjr11.eventora.data.mapper

import com.halimjr11.eventora.data.model.EventResponse
import com.halimjr11.eventora.domain.model.EventDomain

interface RemoteDataMapper {
    suspend fun mapEventResponseToDomain(eventResponse: EventResponse): EventDomain
}