package com.halimjr11.eventora.data.mapper.impl

import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.data.mapper.EventDataMapper
import com.halimjr11.eventora.data.model.EventResponse
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.ui.helper.orZero
import com.halimjr11.eventora.utils.Constants.DATE_DEFAULT
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventDataMapperImpl(private val dispatcher: CoroutineDispatcherProvider) : EventDataMapper {
    // Thread-safe and reusable
    private val inputFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val outputFormatter =
        DateTimeFormatter.ofPattern("MMMM d, yyyy · h:mm a", Locale.getDefault())

    private fun formatDateTime(input: String): String {
        return try {
            val dateTime = LocalDateTime.parse(input, inputFormatter)
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            input // fallback to raw string if parsing fails
        }
    }

    override suspend fun mapEventResponseToDomain(
        eventResponse: EventResponse
    ): EventDomain = withContext(dispatcher.io) {
        EventDomain(
            id = eventResponse.id.orZero(),
            name = eventResponse.name.orEmpty(),
            summary = eventResponse.summary.orEmpty(),
            description = eventResponse.description.orEmpty(),
            imageLogo = eventResponse.imageLogo.orEmpty(),
            mediaCover = eventResponse.mediaCover.orEmpty(),
            category = eventResponse.category.orEmpty(),
            ownerName = eventResponse.ownerName.orEmpty(),
            cityName = eventResponse.cityName.orEmpty(),
            quota = eventResponse.quota.orZero() - eventResponse.registrants.orZero(),
            beginTime = eventResponse.beginTime?.let {
                formatDateTime(it)
            } ?: DATE_DEFAULT,
            endTime = eventResponse.endTime?.let {
                formatDateTime(it)
            } ?: DATE_DEFAULT,
            link = eventResponse.link.orEmpty()
        )
    }
}