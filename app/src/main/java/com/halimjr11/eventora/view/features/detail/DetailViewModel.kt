package com.halimjr11.eventora.view.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.domain.repository.EventRemoteRepository
import com.halimjr11.eventora.utils.Constants.UNKNOWN_ERROR
import com.halimjr11.eventora.utils.DomainResult
import com.halimjr11.eventora.utils.UiState
import com.halimjr11.eventora.utils.UiState.Error
import com.halimjr11.eventora.utils.UiState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: EventRemoteRepository,
    private val localRepository: EventLocalRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _detailEvent = MutableStateFlow<UiState<EventDomain>>(UiState.Loading)
    val detailEvent = _detailEvent.asStateFlow()
    private val _isFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    var idDetail: Int = 0
        private set

    fun setId(id: Int) {
        idDetail = id
        checkFavorite(id)
        loadDetailEvent()
    }

    fun checkFavorite(id: Int) = viewModelScope.launch(dispatcher.io) {
        val result = localRepository.checkIfEventExist(id)
        _isFavorite.value = result
    }

    fun loadDetailEvent() = viewModelScope.launch(dispatcher.io) {
        when (val result = repository.getEventDetail(idDetail)) {
            is DomainResult.Success -> _detailEvent.value = Success(result.data)
            else -> _detailEvent.value = Error(UNKNOWN_ERROR)
        }
    }

    fun toggleFavorite(
        eventDomain: EventDomain
    ) = viewModelScope.launch(dispatcher.io) {
        if (_isFavorite.value) {
            localRepository.deleteEvent(eventDomain)
        } else {
            localRepository.insertEvent(eventDomain)
        }
        _isFavorite.value = !_isFavorite.value
    }
}

