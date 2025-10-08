package com.halimjr11.eventora.domain.usecase

import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.data.repository.EventRepository
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.utils.Constants.EVENT_LIMIT
import com.halimjr11.eventora.utils.Constants.UPCOMING_LIMIT
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
            val resultAll = (repository.getAllEvents() as? DomainResult.Success)?.data.orEmpty()
            val checkAll = resultAll.filter {
                !resultUpcoming.map { eventDomain -> eventDomain.id }.toSet().contains(it.id)
            }
            println("JALANAN -->> check DATA = ${checkAll.size}")
            return@withContext if (resultAll.isNotEmpty() || resultUpcoming.isNotEmpty()) {
                DomainResult.Success(
                    Pair(
                        resultUpcoming.take(EVENT_LIMIT),
                        checkAll.take(UPCOMING_LIMIT)
                    )
                )
            } else DomainResult.Error
        }
}