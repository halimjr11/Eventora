package com.halimjr11.eventora.view.features.detail

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

class DetailViewModel(
    private val repository: EventRepository,
    private val dispatcher: CoroutineDispatcherProvider,
//    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _detailEvent = MutableStateFlow<UiState<EventDomain>>(UiState.Loading)
    val detailEvent = _detailEvent.asStateFlow()

    var idDetail: Int = 0
        private set

    fun setId(id: Int) {
        idDetail = id
        loadDetailEvent()
    }

    fun loadDetailEvent() = viewModelScope.launch(dispatcher.io) {
        when (val result = repository.getEventDetail(idDetail)) {
            is DomainResult.Success -> _detailEvent.value = Success(result.data)
            else -> _detailEvent.value = Error(UNKNOWN_ERROR)
        }
    }
}

