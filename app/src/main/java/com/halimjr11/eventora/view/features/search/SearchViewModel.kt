package com.halimjr11.eventora.view.features.search

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

class SearchViewModel(
    private val repository: EventRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _searchEvents = MutableStateFlow<UiState<List<EventDomain>>>(UiState.Idle)
    val searchEvents = _searchEvents.asStateFlow()
    var query: String = ""
        private set

    fun setQuerySearch(q: String) {
        query = q
        loadSearchEvents()
    }

    fun loadSearchEvents() = viewModelScope.launch(dispatcher.io) {
        _searchEvents.value = UiState.Loading
        when (val result = repository.searchEvents(query)) {
            is DomainResult.Success -> _searchEvents.value = Success(result.data)
            else -> _searchEvents.value = Error(UNKNOWN_ERROR)
        }
    }
}