package com.halimjr11.eventora.view.features.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.data.repository.EventRepository
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.utils.Constants.UNKNOWN_ERROR
import com.halimjr11.eventora.utils.DomainResult
import com.halimjr11.eventora.utils.UiState
import com.halimjr11.eventora.utils.UiState.Error
import com.halimjr11.eventora.utils.UiState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinishedViewModel(
    private val repository: EventRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _pastEvents = MutableStateFlow<UiState<List<EventDomain>>>(UiState.Loading)
    val pastEvents = _pastEvents.asStateFlow()

    init {
        loadPastEvents()
    }

    fun loadPastEvents() = viewModelScope.launch(dispatcher.io) {
        _pastEvents.value = when (val result = repository.getPastEvents()) {
            is DomainResult.Success -> Success(result.data)
            else -> Error(UNKNOWN_ERROR)
        }
    }
}