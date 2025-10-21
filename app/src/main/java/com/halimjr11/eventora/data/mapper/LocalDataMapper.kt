package com.halimjr11.eventora.data.mapper

import com.halimjr11.eventora.data.db.entity.EventEntity
import com.halimjr11.eventora.domain.model.EventDomain

interface LocalDataMapper {
    suspend fun mapEntityToDomain(event: EventEntity): EventDomain
    suspend fun mapDomainToEntity(event: EventDomain): EventEntity
}