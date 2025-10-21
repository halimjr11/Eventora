package com.halimjr11.eventora.view.features.finished.viewmodel

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

class FinishedViewModel(
    private val repository: EventRemoteRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _pastEvents = MutableStateFlow<UiState<List<EventDomain>>>(UiState.Loading)
    val pastEvents = _pastEvents.asStateFlow()

    init {
        loadPastEvents()
    }

    fun loadPastEvents() = viewModelScope.launch(dispatcher.io) {
        _pastEvents.value = when (val result = repository.getPastEvents()) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error(Constants.UNKNOWN_ERROR)
        }
    }
}