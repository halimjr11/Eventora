package com.halimjr11.eventora.data.mapper.impl

import com.halimjr11.eventora.data.db.entity.EventEntity
import com.halimjr11.eventora.data.mapper.LocalDataMapper
import com.halimjr11.eventora.domain.model.EventDomain

class LocalDataMapperImpl : LocalDataMapper {
    override suspend fun mapEntityToDomain(event: EventEntity): EventDomain {
        return EventDomain(
            id = event.id,
            name = event.name,
            summary = event.summary,
            description = event.description,
            imageLogo = event.imageLogo,
            mediaCover = event.mediaCover,
            category = event.category,
            ownerName = event.ownerName,
            cityName = event.cityName,
            quota = event.quota,
            beginTime = event.beginTime,
            endTime = event.endTime,
            link = event.link
        )
    }

    override suspend fun mapDomainToEntity(event: EventDomain): EventEntity {
        return EventEntity(
            id = event.id,
            name = event.name,
            summary = event.summary,
            description = event.description,
            imageLogo = event.imageLogo,
            mediaCover = event.mediaCover,
            category = event.category,
            ownerName = event.ownerName,
            cityName = event.cityName,
            quota = event.quota,
            beginTime = event.beginTime,
            endTime = event.endTime,
            link = event.link
        )
    }

}