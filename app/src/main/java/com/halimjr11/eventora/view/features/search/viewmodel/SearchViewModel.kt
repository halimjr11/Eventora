package com.halimjr11.eventora.view.features.search.viewmodel

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

class SearchViewModel(
    private val repository: EventRemoteRepository,
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
            is DomainResult.Success -> _searchEvents.value = UiState.Success(result.data)
            else -> _searchEvents.value = UiState.Error(Constants.UNKNOWN_ERROR)
        }
    }
}