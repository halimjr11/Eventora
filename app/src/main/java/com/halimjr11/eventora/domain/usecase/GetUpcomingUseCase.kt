package com.halimjr11.eventora.domain.usecase

import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.data.repository.EventRepository
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.utils.Constants.EVENT_LIMIT
import com.halimjr11.eventora.utils.DomainResult
import kotlinx.coroutines.withContext

class GetUpcomingUseCase(
    private val repository: EventRepository,
    private val dispatcher: CoroutineDispatcherProvider
) {
    suspend operator fun invoke(): DomainResult<Pair<List<EventDomain>, List<EventDomain>>> =
        withContext(dispatcher.io) {
            val resultUpcoming =
                (repository.getUpcomingEvents() as? DomainResult.Success)?.data.orEmpty()
            val resultFinished =
                (repository.getPastEvents() as? DomainResult.Success)?.data.orEmpty()
            return@withContext if (resultUpcoming.isNotEmpty()) {
                DomainResult.Success(
                    Pair(
                        resultUpcoming.take(EVENT_LIMIT),
                        resultFinished.take(EVENT_LIMIT)
                    )
                )
            } else DomainResult.Error
        }
}