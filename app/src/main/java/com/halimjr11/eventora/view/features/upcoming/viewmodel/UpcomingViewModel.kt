package com.halimjr11.eventora.view.features.upcoming.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.domain.repository.EventRemoteRepository
import com.halimjr11.eventora.utils.Constants
import com.halimjr11.eventora.utils.DomainResult
import com.halimjr11.eventora.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpcomingViewModel(
    private val repository: EventRemoteRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _upcomingEvents = MutableStateFlow<UiState<List<EventDomain>>>(UiState.Loading)
    val upcomingEvents = _upcomingEvents.asStateFlow()

    init {
        loadUpcomingEvents()
    }

    fun loadUpcomingEvents() = viewModelScope.launch(dispatcher.io) {
        _upcomingEvents.value = when (val result = repository.getUpcomingEvents()) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error(Constants.UNKNOWN_ERROR)
        }
    }
}